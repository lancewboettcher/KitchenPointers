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

		String foods = "Chicken skinless boneless breast";
		
		ArrayList<Integer> ingredientIDs = new ArrayList<Integer>();
		ingredientIDs.add(getIngredientID(conn, foods));
		
		System.out.println("Ingredient IDs Found: " + ingredientIDs);
		
		String ingredientIDString = "01152";
				
		ArrayList<Recipe> recipes = executeRecipeQuery(conn, ingredientIDString);

		return recipes;
	}
	
	@Override
	public void addRecipe(Recipe recipe) {
		MakeSQL makeSQL = new MakeSQL();
		
		
		int[] ingredients = {05746, 04053, 11215, 19334 };
		String[] quantities = { "4", "4", "4", "1" };
		String URL = "http://www.food.com/recipe/easy-garlic-chicken-5478";
		String cuisine = "Asian";
		String name = "Garlic Chicken";
		makeSQL.makeAddRecipe(2, ingredients, quantities, name, cuisine, URL);
		
	}

}
