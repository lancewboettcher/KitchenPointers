package com.kitchenpointers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

	private int getIngredientID(Connection conn, String ingredient) {
		ResultSet rst;
		PreparedStatement pst;
		
		MakeSQL makeSQL = new MakeSQL(ingredient);
		String NDBQuery = makeSQL.makeNDBQuery();
		
		int ingredientID = 0;
		
		System.out.println("NDB QUERY: \n" + NDBQuery);

		try {
			pst = conn.prepareStatement(NDBQuery);
			rst = pst.executeQuery();
			
			if (rst.next()) {
				ingredientID = rst.getInt("NDB_No");
			}
			else {
				System.out.println("Ingredient Not Found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ingredientID;
	}
	
	private String getIngredientIDString(Connection conn, String ingredient) {
		ResultSet rst;
		PreparedStatement pst;
		
		if (ingredient.contains("'")) {
			ingredient = ingredient.replace("'", "\\'");
		}
		System.out.println(ingredient);
		
		MakeSQL makeSQL = new MakeSQL(ingredient);
		String NDBQuery = makeSQL.makeNDBQuery();
		
		String ingredientID = "";
		
		System.out.println("NDB QUERY: \n" + NDBQuery);

		try {
			pst = conn.prepareStatement(NDBQuery);
			rst = pst.executeQuery();
			
			if (rst.next()) {
				ingredientID = rst.getString("NDB_No");
			}
			else {
				System.out.println("Ingredient Not Found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ingredientID;
	}
	
	private ArrayList<Recipe> executeRecipeQuery(Connection conn, String ingredientIDs) {
		ResultSet rst;
		PreparedStatement pst;
		
		MakeSQL makeSQL = new MakeSQL(ingredientIDs);
		String recipeQuery = makeSQL.makeRecipeDBQuery();
		
		System.out.println("RECIPE QUERY: \n" + recipeQuery);

		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<Ingredient> ingredients;
		
		try {
			pst = conn.prepareStatement(recipeQuery);
			rst = pst.executeQuery();

			// Populate Recipe Info
			while (rst.next()) {
				recipes.add(new Recipe(rst.getInt("recipeID"), rst.getString("recipeName"), rst.getInt("calorieCount"),
						rst.getInt("fatCount"), rst.getInt("sugarCount"), rst.getInt("proteinCount"),
						rst.getString("cuisine"), 0, rst.getString("URL"), null));
			}
			
			//Populate Ingredient Info 
			for(int i = 0; i < recipes.size(); i++) {
				ingredients = new ArrayList<Ingredient>();
				
				String query = "SELECT r.recipeID, r.ingredientID, r.quantity, n.Shrt_Desc "
						+ "FROM recipeIngredients r "
						+ "JOIN nutritionFacts n "
						+ "ON r.ingredientID=n.NDB_No "
						+ "WHERE recipeID=" + recipes.get(i).getId();
				
				System.out.println("Get ings q: \n" + query);
				
				pst = conn.prepareStatement(query);
				rst = pst.executeQuery();
				
				while (rst.next()) {
					ingredients.add(new Ingredient(	rst.getInt("ingredientID"), 
													rst.getString("Shrt_Desc"), 
													0.0, 
													rst.getString("quantity"))
								   );
				}
				
				recipes.get(i).setIngredients(ingredients);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return recipes;
	}

	@Override
	public ArrayList<Recipe> getRecipes(SearchCriteria criteria) {
		Connection conn = getConnection();
		
		String includedIngredientIDs = "";
		
		for (String ingredient : criteria.getIncludedIngredients()) {
			includedIngredientIDs += Integer.toString(getIngredientID(conn, ingredient)) + " ";
		}
		
		includedIngredientIDs.trim();
		
		System.out.println("Ingredient IDs Found: " + includedIngredientIDs);
		
	//	String ingredientIDString = "01152";
				
		ArrayList<Recipe> recipes = executeRecipeQuery(conn, includedIngredientIDs);

		return recipes;
	}
	
	@Override
	public void addRecipe(Recipe recipe) {
		Connection conn = getConnection();
		PreparedStatement pst;
		
		try {
			//Create the Recipe
			pst = conn.prepareStatement("INSERT into recipeDB values(?, ?, ?, NULL, NULL, NULL, NULL,?);");
			pst.setInt(1, recipe.getId());
			pst.setString(2, recipe.getName());
			pst.setString(3, recipe.getCuisine());
			pst.setString(4, recipe.getUrl());
			
			pst.executeUpdate();
			
			//Add all ingredients
			for (Ingredient ingredient : recipe.getIngredients()) {
				pst = conn.prepareStatement("INSERT into recipeIngredients values(?,?,?);");
				pst.setInt(1, recipe.getId());
				pst.setString(2,getIngredientIDString(conn, ingredient.getName()));
				pst.setString(3, Double.toString(ingredient.getQuantity()));
				
				pst.executeUpdate();
			}
			
			//Update the nutrition facts 
			pst = conn.prepareStatement("UPDATE recipeDB SET proteinCount= (SELECT SUM(P.Protein) "
					+ "FROM (SELECT t2.Protein FROM recipeIngredients as t1 Join nutritionFacts as t2 "
					+ "ON t1.ingredientID= t2.NDB_No WHERE t1.recipeID=?) as P) "
					+ "WHERE recipeID=?;");
			pst.setInt(1, recipe.getId());
			pst.setInt(2, recipe.getId());
			
			pst.executeUpdate();
			
			pst = conn.prepareStatement("UPDATE recipeDB set calorieCount = (SELECT SUM(P.Energ_Kcal) "
					+ "FROM (SELECT t2.Energ_Kcal from recipeIngredients as t1 "
					+ "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No "
					+ "WHERE t1.recipeID=?) as P) "
					+ "WHERE recipeID =?;");
			pst.setInt(1, recipe.getId());
			pst.setInt(2, recipe.getId());
			
			pst.executeUpdate();

			pst = conn.prepareStatement("UPDATE recipeDB set fatCount = (SELECT SUM(P.Lipid_tot) "
					+ "FROM (SELECT t2.Lipid_tot from recipeIngredients as t1 "
					+ "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No "
					+ "WHERE t1.recipeID=?) as P) "
					+ "WHERE recipeID =?;");
			pst.setInt(1, recipe.getId());
			pst.setInt(2, recipe.getId());
			
			pst.executeUpdate();
			
			pst = conn.prepareStatement("UPDATE recipeDB set sugarCount = (SELECT SUM(P.Sugar_tot) "
					+ "FROM (SELECT t2.Sugar_tot from recipeIngredients as t1 "
					+ "JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No "
					+ "WHERE t1.recipeID=?) as P) "
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
			//Delete the ingredients
			pst = conn.prepareStatement("DELETE FROM recipeIngredients WHERE recipeID = ?;");
			pst.setInt(1, recipeId);

			pst.executeUpdate();
			
			//Delete the Recipe
			pst = conn.prepareStatement("DELETE FROM recipeDB WHERE recipeID = ?;");
			pst.setInt(1, recipeId);

			pst.executeUpdate();

			System.out.println("Recipe Deleted From Database");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
