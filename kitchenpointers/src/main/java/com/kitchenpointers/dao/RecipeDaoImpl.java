package com.kitchenpointers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.kitchenpointers.domain.Ingredient;
import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;

public class RecipeDaoImpl implements RecipeDao {

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://aac3cbjoeutp2a.czmvfbntc6xb.us-west-2.rds.amazonaws.com:3306/kitchenPointers?user=kitchenpointers&password=fkurfess");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }
    
    private String getIngredientIDString(Connection conn, String ingredient) {
        ResultSet rst;
        PreparedStatement pst;

        if (ingredient.contains("'")) {
            ingredient = ingredient.replace("'", "\\'");
        }
        System.out.println(ingredient);

        String nutritionDBQuery = MakeSQL.getNutritionDBQuery(ingredient);

        String ingredientID = "";

        System.out.println("NDB QUERY: \n" + nutritionDBQuery);

        try {
            pst = conn.prepareStatement(nutritionDBQuery);
            rst = pst.executeQuery();

            if (rst.next()) {
                ingredientID = rst.getString("NDB_No");
            } else {
                System.out.println("Ingredient Not Found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ingredientID;
    }

    // Returns all possible ingredient ids in our recipeIngredients table
    // containing string
    private ArrayList<String> getIngredientIds(Connection conn, String ingredient) {
        ArrayList<String> ingredientIds = new ArrayList<String>();
        ResultSet rst;
        PreparedStatement pst;

        if (ingredient.contains("'")) {
            ingredient = ingredient.replace("'", "\\'");
        }

        String[] ingredientWords = ingredient.split(" ");

        /*
         * String query =
         * "SELECT DISTINCT ingredientID FROM recipeIngredients r " +
         * "JOIN nutritionFacts n ON r.ingredientID=n.NDB_No " +
         * "WHERE n.Shrt_Desc LIKE '%" + ingredientWords[0] + "%'";
         */
        String query = "SELECT DISTINCT ingredientID FROM recipeIngredients r "
                + "JOIN nutritionFacts n ON r.ingredientID=n.NDB_No " + "WHERE n.Shrt_Desc LIKE '%" + ingredientWords[0]
                + "%'";

        for (int i = 1; i < ingredientWords.length; i++) {
            query = query + " AND n.Shrt_Desc LIKE '%" + ingredientWords[i] + "%'";
        }

        query = query + ";";

        System.out.println("Get ingredientIDs query: \n" + query);

        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();

            while (rst.next()) {
                ingredientIds.add(rst.getString("ingredientID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Ingredient IDs found: \n" + ingredientIds);

        return ingredientIds;
    }
    
    public ArrayList<String> getFoodTypeOntologicalRelationship(Connection conn, String ingredient) {
    	ArrayList<String> foodGroupIds = new ArrayList<String>();
        ResultSet rst;
        PreparedStatement pst;

        if (ingredient.contains("'")) {
            ingredient = ingredient.replace("'", "\\'");
        }

        String[] ingredientWords = ingredient.split(" ");
        
        String query = "SELECT nutritionFacts.NDB_No, nutritionFacts.Shrt_Desc, "
    			+ "foodGroups.FdGrp_Cd, foodGroups.FdGrp_Desc "
    			+ "FROM nutritionFacts JOIN foodTypeOntologicalRelationships ON "
    			+ "nutritionFacts.NDB_No = foodTypeOntologicalRelationships.NDB_No "
    			+ "JOIN foodGroups ON foodGroups.FdGrp_Cd = foodTypeOntologicalRelationships.FdGrp_Cd "
    			+ "WHERE nutritionFacts.Shrt_Desc LIKE '%" + ingredientWords[0]
    	        + "%'";
        
        for (int i = 1; i < ingredientWords.length; i++) {
            query = query + " AND n.Shrt_Desc LIKE '%" + ingredientWords[i] + "%'";
        }
        
        query = query + " GROUP BY foodGroups.FdGrp_Cd";
        
        System.out.println("Get ingredientIDs query: \n" + query);

        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();

            while (rst.next()) {
                foodGroupIds.add(rst.getString("FdGrp_Cd"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Food Groups found: \n" + foodGroupIds);

        return foodGroupIds;
    }

    @Override
    public int addRecipe(Recipe recipe) {
        Connection conn = getConnection();
        PreparedStatement pst;
        ResultSet rst;
        
        recipe.setId(null); //Don't want to accidentally delete an existing recipe

        try {
        	//Get the next recipeID
			pst = conn.prepareStatement("SELECT MAX(recipeID) from recipeDB;");
			rst = pst.executeQuery();
			
			if (rst.next()) {
				recipe.setId(rst.getInt("MAX(recipeID)") + 1);
			}
        	
            // Create the Recipe
            pst = conn.prepareStatement("INSERT into recipeDB values(?, ?, ?, NULL, NULL, NULL, NULL,?);");
            pst.setInt(1, recipe.getId());
            pst.setString(2, recipe.getName());
            pst.setString(3, recipe.getCuisine());
            pst.setString(4, recipe.getUrl());

            pst.executeUpdate();

            // Add all ingredients
            for (Ingredient ingredient : recipe.getIngredients()) {
                pst = conn.prepareStatement("INSERT into recipeIngredients values(?,?,?,?);");
                pst.setInt(1, recipe.getId());
                pst.setString(2, getIngredientIDString(conn, ingredient.getName()));
                pst.setDouble(3, ingredient.getQuantity());
                pst.setString(4, ingredient.getUnit());

                pst.executeUpdate();
            }

            // Update the nutrition facts
            pst = conn.prepareStatement("UPDATE recipeDB SET proteinCount= (SELECT SUM(P.Protein) "
                    + "FROM (SELECT t2.Protein FROM recipeIngredients as t1 Join nutritionFacts as t2 "
                    + "ON t1.ingredientID= t2.NDB_No WHERE t1.recipeID=?) as P) " + "WHERE recipeID=?;");
            pst.setInt(1, recipe.getId());
            pst.setInt(2, recipe.getId());

            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE recipeDB set calorieCount = (SELECT SUM(P.Energ_Kcal) "
                    + "FROM (SELECT t2.Energ_Kcal from recipeIngredients as t1 "
                    + "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No " + "WHERE t1.recipeID=?) as P) "
                    + "WHERE recipeID =?;");
            pst.setInt(1, recipe.getId());
            pst.setInt(2, recipe.getId());

            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE recipeDB set fatCount = (SELECT SUM(P.Lipid_tot) "
                    + "FROM (SELECT t2.Lipid_tot from recipeIngredients as t1 "
                    + "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No " + "WHERE t1.recipeID=?) as P) "
                    + "WHERE recipeID =?;");
            pst.setInt(1, recipe.getId());
            pst.setInt(2, recipe.getId());

            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE recipeDB set sugarCount = (SELECT SUM(P.Sugar_tot) "
                    + "FROM (SELECT t2.Sugar_tot from recipeIngredients as t1 "
                    + "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No " + "WHERE t1.recipeID=?) as P) "
                    + "WHERE recipeID =?;");
            pst.setInt(1, recipe.getId());
            pst.setInt(2, recipe.getId());

            pst.executeUpdate();

            System.out.println("Recipe Added To Database");

        } catch (Exception ex) {
            deleteRecipe(recipe.getId());

            ex.printStackTrace();
        }
        
        try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return recipe.getId();
    }

    @Override
    public void deleteRecipe(int recipeId) {
        Connection conn = getConnection();
        PreparedStatement pst;

        try {
            // Delete the ingredients
            pst = conn.prepareStatement("DELETE FROM recipeIngredients WHERE recipeID = ?;");
            pst.setInt(1, recipeId);

            pst.executeUpdate();

            // Delete the Recipe
            pst = conn.prepareStatement("DELETE FROM recipeDB WHERE recipeID = ?;");
            pst.setInt(1, recipeId);

            pst.executeUpdate();

            System.out.println("Recipe Deleted From Database");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private ArrayList<Recipe> getRecipeIds(Connection conn, ArrayList<ArrayList<String>> ingredientIDs) {
        ResultSet rst;
        PreparedStatement pst;

        // TODO: Change this to check for recipes containing all ingredients
        ArrayList<String> singleArrayIds = new ArrayList<String>();
        for (ArrayList<String> ids : ingredientIDs) {
            singleArrayIds.addAll(ids);
        }

        String recipeIDQuery = MakeSQL.getRecipeIDsQuery(singleArrayIds);
        System.out.println("RECIPE ID QUERY: \n" + recipeIDQuery);

        HashMap<Integer, Integer> recipeIds = new HashMap<Integer, Integer>();
        HashMap<Integer, Recipe> recipes = new HashMap<Integer, Recipe>();
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        ArrayList<Ingredient> ingredients;
        float numIDs = ingredientIDs.size();
        String recipeQuery = "";

        try {

            // Get all recipeIDs
            pst = conn.prepareStatement(recipeIDQuery);
            rst = pst.executeQuery();

            while (rst.next()) {
                recipeIds.put(rst.getInt("recipeID"), rst.getInt("count"));
            }

            recipeQuery = MakeSQL.getRecipesQuery(recipeIds.keySet());

            System.out.println("Recipe Query: \n" + recipeQuery);

            // Get all recipes from ids
            pst = conn.prepareStatement(recipeQuery);
            rst = pst.executeQuery();

            // Populate Recipe Info
            while (rst.next()) {
                int currID = rst.getInt("recipeID");
                int count = recipeIds.get(currID);
                Recipe addedRecipe = new Recipe(currID, rst.getString("recipeName"), rst.getInt("calorieCount"),
                        rst.getInt("fatCount"), rst.getInt("sugarCount"), rst.getInt("proteinCount"),
                        rst.getString("cuisine"), 0, (new Float(count) / numIDs), rst.getString("URL"), null);
                recipes.put(currID, addedRecipe);
            }

            // Populate Ingredient Info
            for (Recipe recipe : recipes.values()) {
                ingredients = new ArrayList<Ingredient>();

                String query = "SELECT r.recipeID, r.ingredientID, r.quantityNum, r.quantityUnit, n.Shrt_Desc "
                        + "FROM recipeIngredients r " + "JOIN nutritionFacts n " + "ON r.ingredientID=n.NDB_No "
                        + "WHERE recipeID =" + recipe.getId() + ";";

                System.out.println("Get ings q: \n" + query);

                pst = conn.prepareStatement(query);
                rst = pst.executeQuery();

                while (rst.next()) {
                    ingredients.add(new Ingredient(rst.getInt("ingredientID"), rst.getString("Shrt_Desc"),
                            rst.getDouble("quantityNum"), rst.getString("quantityUnit")));
                }

                recipe.setIngredients(ingredients);
                recipeList.add(recipe);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    // Determines whether the provided recipe meets the calorie criteria (is
    // under or equal to the calorie count)
    // Returns false if the recipe has more calories than the calorie criteria
    private boolean checkCals(Recipe recipe, int calories) {
        boolean isValid = true;
        if (calories != 0 && recipe.getCalories() > calories) {
            isValid = false;
        }
        return isValid;
    }

    // Determines whether the provided recipe's cuisine matches the cuisine
    // criteria
    // Returns false if cuisine does not equal the recipe's cuisine
    private boolean checkCuisine(Recipe recipe, String cuisine) {
        boolean isValid = true;
        if (cuisine != null && !recipe.getCuisine().equalsIgnoreCase(cuisine)) {
            isValid = false;
        }
        return isValid;
    }

    // Determines if any of the ingredients in the recipe are excluded
    // ingredients
    // Returns false if the recipes includes excluded ingredients
    private boolean checkIngredients(Recipe recipe, ArrayList<Integer> ingredientIds) {
        boolean isValid = true;
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        for (int idx = 0; idx < ingredients.size() && isValid; idx++) {
            if (ingredientIds.contains(ingredients.get(idx).getIngredientId())) {
                isValid = false;
            }
        }
        return isValid;
    }

    // Post processes the list of recipes to remove any that do not meet the
    // criteria provided
    private ArrayList<Recipe> postProcessing(ArrayList<Recipe> recipes, SearchCriteria criteria,
            ArrayList<Integer> excludedIngredients) {
        ArrayList<Recipe> validRecipes = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            int cals = criteria.getCalories();
            String cuisine = criteria.getCuisine();
            if (checkCals(recipe, cals) && checkCuisine(recipe, cuisine)
                    && checkIngredients(recipe, excludedIngredients)) {
                validRecipes.add(recipe);
            }
        }
        return validRecipes;
    }

    @Override
    public ArrayList<Recipe> getRecipes(SearchCriteria criteria) {
        Connection conn = getConnection();

        ArrayList<ArrayList<String>> includedIngredientIDs = new ArrayList<ArrayList<String>>();
        ArrayList<String> ingredientIds;
        ArrayList<Integer> excludedIngredientIDs = new ArrayList<Integer>();

        for (String ingredient : criteria.getIncludedIngredients()) {
        	//Get all ingredient IDs in our DB matching that string
        	ingredientIds = getIngredientIds(conn, ingredient);
        	
        	if (!ingredientIds.isEmpty())
        		includedIngredientIDs.add(ingredientIds);
        }
        
        if (includedIngredientIDs.isEmpty()) {
        	//No matching included ingredients in our database
        	return null;
        }
        
        System.out.println("Included ingredients: \n" + includedIngredientIDs);

        // Get all excluded ingredient IDs and add them to ArrayList as Integers
        // for post processing
        for (String ingredient : criteria.getExcludedIngredients()) {
            ArrayList<String> excludedStrings = getIngredientIds(conn, ingredient);
            for (String str : excludedStrings) {
                excludedIngredientIDs.add(new Integer(str));
            }
        }

        System.out.println("Excluded ingredients: \n" + excludedIngredientIDs);

        // Get recipes for requested ingredients
        ArrayList<Recipe> recipes = getRecipeIds(conn, includedIngredientIDs);

        // Post process recipe list to remove any with invalid criteria
        // (excluded ingredients, cuisine, calories, etc.)
        ArrayList<Recipe> validRecipes = postProcessing(recipes, criteria, excludedIngredientIDs);

        try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return validRecipes;
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        Connection conn = getConnection();

        ResultSet rst;
        PreparedStatement pst;

        String recipeQuery = "SELECT * FROM recipeDB WHERE recipeID = " + recipeId + ";";

        System.out.println("Recipe Query: \n" + recipeQuery);

        Recipe recipe = new Recipe();
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            pst = conn.prepareStatement(recipeQuery);
            rst = pst.executeQuery();

            // Populate Recipe Info
            if (rst.next()) {
                recipe = new Recipe(recipeId, rst.getString("recipeName"), rst.getInt("calorieCount"),
                        rst.getInt("fatCount"), rst.getInt("sugarCount"), rst.getInt("proteinCount"),
                        rst.getString("cuisine"), 0, 0, rst.getString("URL"), null);
            } else {
            	return null;
            }

            // Populate Ingredient Info
            String query = "SELECT r.recipeID, r.ingredientID, r.quantityNum, r.quantityUnit, n.Shrt_Desc "
                    + "FROM recipeIngredients r " + "JOIN nutritionFacts n " + "ON r.ingredientID=n.NDB_No "
                    + "WHERE recipeID =" + recipe.getId() + ";";

            System.out.println("Get ings q: \n" + query);

            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();

            while (rst.next()) {
                ingredients.add(new Ingredient(rst.getInt("ingredientID"), rst.getString("Shrt_Desc"),
                        rst.getDouble("quantityNum"), rst.getString("quantityUnit")));
            }

            recipe.setIngredients(ingredients);
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        
        try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        return recipe;
    }

}
