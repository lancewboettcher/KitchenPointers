package com.kitchenpointers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    //Returns all possible ingredient ids in our recipeIngredients table  containing string
    private ArrayList<String> getIngredientIds(Connection conn, String ingredient) {
    	ArrayList<String> ingredientIds = new ArrayList<String>();
    	ResultSet rst;
        PreparedStatement pst;

        if (ingredient.contains("'")) {
            ingredient = ingredient.replace("'", "\\'");
        }
        
        String[] ingredientWords = ingredient.split(" ");
        
        String query = "SELECT DISTINCT ingredientID FROM recipeIngredients r "
        		+ "JOIN nutritionFacts n ON r.ingredientID=n.NDB_No "
        		+ "WHERE n.Shrt_Desc LIKE '%" + ingredientWords[0] + "%'";
        
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

    @Override
    public void addRecipe(Recipe recipe) {
        Connection conn = getConnection();
        PreparedStatement pst;

        try {
            // Create the Recipe
            pst = conn.prepareStatement("INSERT into recipeDB values(?, ?, ?, NULL, NULL, NULL, NULL,?);");
            pst.setInt(1, recipe.getId());
            pst.setString(2, recipe.getName());
            pst.setString(3, recipe.getCuisine());
            pst.setString(4, recipe.getUrl());

            pst.executeUpdate();

            // Add all ingredients
            for (Ingredient ingredient : recipe.getIngredients()) {
                pst = conn.prepareStatement("INSERT into recipeIngredients values(?,?,?);");
                pst.setInt(1, recipe.getId());
                pst.setString(2, getIngredientIDString(conn, ingredient.getName()));
                pst.setString(3, Double.toString(ingredient.getQuantity()));

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
    }

    private ArrayList<Recipe> getRecipeIds(Connection conn, ArrayList<ArrayList<String>> ingredientIDs) {
        ResultSet rst;
        PreparedStatement pst;

        //TODO: Change this
        ArrayList<String> singleArrayIds = new ArrayList<String>();
        for (ArrayList<String> ids : ingredientIDs) {
        	for (String id : ids)
        		singleArrayIds.add(id);
        }
        
        String recipeQuery = MakeSQL.getRecipeDBQuery(singleArrayIds);
        System.out.println("RECIPE QUERY: \n" + recipeQuery);

        HashMap<Integer, Recipe> recipes = new HashMap<Integer, Recipe>();
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        ArrayList<Ingredient> ingredients;
        float numIDs = ingredientIDs.size();

        try {
            pst = conn.prepareStatement(recipeQuery);
            rst = pst.executeQuery();

            // Populate Recipe Info
            while (rst.next()) {
                int currID = rst.getInt("recipeID");
                if (recipes.containsKey(currID)) {
                    Recipe currRecipe = recipes.get(currID);
                    float matchScore = currRecipe.getMatchScore();
                    currRecipe.setMatchScore(matchScore + (1.0f / numIDs));
                } else {
                    Recipe addedRecipe = new Recipe(currID, rst.getString("recipeName"), rst.getInt("calorieCount"),
                            rst.getInt("fatCount"), rst.getInt("sugarCount"), rst.getInt("proteinCount"),
                            rst.getString("cuisine"), 0, (1.0f / numIDs), rst.getString("URL"), null);
                    recipes.put(currID, addedRecipe);
                }
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
                    ingredients.add(new Ingredient(rst.getInt("ingredientID"), rst.getString("Shrt_Desc"), rst.getDouble("quantityNum"),
                            rst.getString("quantityUnit")));
                }

                recipe.setIngredients(ingredients);
                recipeList.add(recipe);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    private boolean checkCals(Recipe recipe, int calories) {
        boolean isValid = true;
        if (calories != 0 && recipe.getCalories() > calories) {
            isValid = false;
        }
        return isValid;
    }

    private boolean checkCuisine(Recipe recipe, String cuisine) {
        boolean isValid = true;
        if (cuisine != null && !recipe.getCuisine().equalsIgnoreCase(cuisine)) {
            isValid = false;
        }
        return isValid;
    }

    private ArrayList<Recipe> postProcessing(ArrayList<Recipe> recipes, SearchCriteria criteria) {
        ArrayList<Recipe> validRecipes = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            int cals = criteria.getCalories();
            String cuisine = criteria.getCuisine();
            if (checkCals(recipe, cals) && checkCuisine(recipe, cuisine)) {
                validRecipes.add(recipe);
            }
        }
        return validRecipes;
    }

    @Override
    public ArrayList<Recipe> getRecipes(SearchCriteria criteria) {
        Connection conn = getConnection();

        ArrayList<ArrayList<String>> includedIngredientIDs = new ArrayList<ArrayList<String>>();
        ArrayList<String> excludedIngredientIDs = new ArrayList<String>();

        for (String ingredient : criteria.getIncludedIngredients()) {
            includedIngredientIDs.add(getIngredientIds(conn, ingredient));
        }
        
        System.out.println("Included ingredients: \n" + includedIngredientIDs);

        for (String ingredient : criteria.getExcludedIngredients()) {
            excludedIngredientIDs.add(getIngredientIDString(conn, ingredient));
        }

        includedIngredientIDs.removeAll(excludedIngredientIDs);

        // Get recipes for requested ingredients
        ArrayList<Recipe> recipes = getRecipeIds(conn, includedIngredientIDs);
                
        // Post process recipe list to remove any with invalid criteria (cuisine, calories, etc.)
        ArrayList<Recipe> validRecipes = postProcessing(recipes, criteria);

        return validRecipes;
    }

}
