package com.kitchenpointers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class JDBCConnect {

    private ArrayList<RecipeDBTuple> recipeDBTuples;
    private ArrayList<RecipeIngredientsTuple> recipeIngredientsTuples;
    private ArrayList<NutritionFactTuple> nutritionFactTuples;
    // private ArrayList<NutritionFactTuple> nutritionFactTuples;
    private Connection conn;

    public JDBCConnect() {
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://aac3cbjoeutp2a.czmvfbntc6xb.us-west-2.rds.amazonaws.com:3306/kitchenPointers?user=kitchenpointers&password=fkurfess");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        recipeDBTuples = new ArrayList<RecipeDBTuple>();
        recipeIngredientsTuples = new ArrayList<RecipeIngredientsTuple>();
        nutritionFactTuples = new ArrayList<NutritionFactTuple>();

        populateRecipeDBs();
        populateRecipeIngredients();
        populateNutritionFacts();
    }

    private void populateRecipeDBs() {
        String sql = "SELECT * FROM recipeDB;";

        ResultSet rst;
        PreparedStatement pst;

        try {
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();

            while (rst.next()) {
                recipeDBTuples.add(new RecipeDBTuple(rst.getInt("recipeID"), rst.getString("recipeName"),
                        rst.getString("cuisine"), rst.getDouble("calorieCount"), rst.getDouble("fatCount"),
                        rst.getDouble("sugarCount"), rst.getDouble("proteinCount"), rst.getString("URL")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateRecipeIngredients() {
        String sql = "SELECT * FROM recipeIngredients;";

        ResultSet rst;
        PreparedStatement pst;

        try {
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();

            while (rst.next()) {
                recipeIngredientsTuples.add(new RecipeIngredientsTuple(rst.getInt("recipeID"),
                        rst.getString("ingredientID"), rst.getString("quantity")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateNutritionFacts() {
        String sql = "SELECT * FROM nutritionFacts;";

        ResultSet rst;
        PreparedStatement pst;

        try {
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            while (rst.next()) {
                nutritionFactTuples.add(new NutritionFactTuple());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<RecipeDBTuple> getRecipeDBTuples() {
        return this.recipeDBTuples;
    }

    public ArrayList<RecipeIngredientsTuple> getRecipeIngredientsTuples() {
        return this.recipeIngredientsTuples;
    }

    public ArrayList<NutritionFactTuple> getNutritionFactTuples() {
        return this.nutritionFactTuples;
    }

}
