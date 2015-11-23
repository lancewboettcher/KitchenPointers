package com.kitchenpointers.dao;

import java.util.ArrayList;

public class MakeSQL {
    public String in;
    public String out;

    public MakeSQL() {

    }

    // constructor. pass the thing you're searching for to constructor then call
    // applicable function for the query you want to create.
    public MakeSQL(String in) {
        this.in = "";
        String delims = "[ ,.]+";
        String[] tokens = in.split(delims);
        for (int i = 0; i < tokens.length; i++) {
            // check for numbers and units of measure eventually
            this.in = this.in + tokens[i];
            this.in = this.in + " ";
        }
    }

    // Get the nutritionfacts database number from the user input
    // (must enter in each line of the recipe one at a time)
    public String makeNDBQuery() {
        String sql1 = "SELECT NDB_No, Shrt_Desc,MATCH (Shrt_Desc) AGAINST ('";
        String sql2 = "' IN BOOLEAN MODE) AS score FROM nutritionFacts WHERE MATCH (Shrt_Desc) AGAINST ('";
        String sql3 = "' IN BOOLEAN MODE) HAVING score > 0.5 order by score desc;";
        this.out = sql1 + this.in + sql2 + this.in + sql3;
        // System.out.println(this.in);
        // System.out.println(this.out);
        return this.out;
    }

    public static String getNutritionDBQuery(String ingredient) {
        String sql1 = "SELECT NDB_No, Shrt_Desc,MATCH (Shrt_Desc) AGAINST ('";
        String sql2 = "' IN BOOLEAN MODE) AS score FROM nutritionFacts WHERE MATCH (Shrt_Desc) AGAINST ('";
        String sql3 = "' IN BOOLEAN MODE) HAVING score > 0.5 order by score desc;";
        return sql1 + ingredient + sql2 + ingredient + sql3;
    }

    // Get all recipes that have the requested ingredient in it. Must use
    // MakeNDBQuery first to turn the ingredient into a NDB_N0
    public String makeRecipeDBQuery() {
        String sql1 = "SELECT * FROM recipeDB JOIN recipeIngredients on recipeDB.recipeID = recipeIngredients.recipeID WHERE ingredientID =";
        this.out = sql1 + this.in + ";";
        // System.out.println(this.out);
        return this.out;
    }

    public static String getRecipeDBQuery(ArrayList<String> ingredients) {
        String sql1 = "SELECT * FROM recipeDB JOIN recipeIngredients "
        		+ "ON recipeDB.recipeID = recipeIngredients.recipeID WHERE ingredientID in ";
        String ingredientList = ingredients.toString();
        ingredientList = ingredientList.replace("[", "(").replace("]", ")");
        return sql1 + ingredientList + ";";
    }

    // Make the query to get the next number for the recipeID for the recipeDB
    // database.
    public String getNextRID() {
        return "SELECT max(recipeID) from recipeDB ;";
    }

    // Make the query to insert a new recipe into the database.
    // call getNextRID first so you know what rID should be.
    public String makeAddRecipe(int rID, ArrayList<Integer> ingredient, ArrayList<String> quantity, String name,
            String cuisine, String URL) {
        // this.out = "INSERT into recipeDB values(" + String.valueOf(rID) + ",
        // '" + name +"', '" +cuisine +"', NULL, NULL, NULL, NULL, '" + URL +
        // "'); \n";
        this.out = "";
        String sql1;
        for (int i = 0; i < ingredient.size(); i++) {
            sql1 = "INSERT into recipeIngredients values('";
            this.out = this.out + sql1 + String.valueOf(rID) + "'," + "(select NDB_No from nutritionFacts where NDB_No="
                    + String.valueOf(ingredient.get(i)) + "),'" + quantity.get(i) + "');\n";
        }
        String updateProtein = "UPDATE recipeDB SET proteinCount= (SELECT SUM(P.Protein) FROM (SELECT t2.Protein FROM recipeIngredients as t1 Join nutritionFacts as t2 ON t1.ingredientID= t2.NDB_No WHERE t1.recipeID ="
                + String.valueOf(rID) + ") as P) WHERE recipeID =" + String.valueOf(rID) + ";\n";
        String updateCalorie = "UPDATE recipeDB set calorieCount = (SELECT SUM(P.Energ_Kcal) FROM (SELECT t2.Energ_Kcal from recipeIngredients as t1 JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No WHERE t1.recipeID="
                + String.valueOf(rID) + ") as P) WHERE recipeID =" + String.valueOf(rID) + ";\n";
        String updateFat = "UPDATE recipeDB set fatCount = (SELECT SUM(P.Lipid_tot) FROM (SELECT t2.Lipid_tot from recipeIngredients as t1 JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No WHERE t1.recipeID="
                + String.valueOf(rID) + ") as P) WHERE recipeID =" + String.valueOf(rID) + ";\n";
        String updateSugar = "UPDATE recipeDB set sugarCount = (SELECT SUM(P.Sugar_tot) FROM (SELECT t2.Sugar_tot from recipeIngredients as t1 JOIN nutritionFacts as t2 on t1.ingredientID= t2.NDB_No WHERE t1.recipeID="
                + String.valueOf(rID) + ") as P) WHERE recipeID =" + String.valueOf(rID) + ";\n";
        this.out = this.out + updateProtein + updateCalorie + updateFat + updateSugar;
        // System.out.println(this.out);
        return this.out;
    }

    // query to return all the nouns in a line of a recipe
    public String getKeywords() {
        this.out = "SELECT word FROM moby_thesaurus.word_parts_of_speech where ";
        String[] tokens = this.in.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            this.out = this.out + "word = '" + tokens[i] + "' AND part_of_speech_id = 1 OR ";
        }
        this.out = this.out.substring(0, this.out.lastIndexOf("OR ")) + ";";
        // System.out.println(this.out);
        return this.out;

    }
    /*
     * public static void main(String[] args){ //Scanner sc = new
     * Scanner(System.in); // while sc.hasNext(){
     * 
     * // } String foods = "Chicken skinless boneless breast"; MakeSQL myString
     * = new MakeSQL(foods); myString.makeNDBQuery(); foods = "01152"; myString
     * = new MakeSQL(foods); myString.makeRecipeDBQuery(); int[] ingredients =
     * {05746, 04053, 11215, 19334}; String[] quantities = {"4", "4", "4", "1"};
     * String URL = "http://www.food.com/recipe/easy-garlic-chicken-5478";
     * String cuisine = "Asian"; String name = "Garlic Chicken";
     * myString.makeAddRecipe(2, ingredients, quantities, name, cuisine, URL);
     * 
     * }
     */
}
