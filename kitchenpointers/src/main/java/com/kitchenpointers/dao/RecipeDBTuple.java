package com.kitchenpointers.dao;

class RecipeDBTuple {
	private int recipeID;
	private String recipeName;
	private String cuisine;
	private double calorieCount;
	private double fatCount;
	private double sugarCount;
	private double proteinCount;
	private String URL;

	public RecipeDBTuple() {}
	public RecipeDBTuple(int recipeID, String recipeName, String cuisine, double calorieCount, double fatCount, double sugarCount, double proteinCount, String URL) {
		this.recipeID = recipeID;
		this.recipeName = recipeName;
		this.cuisine = cuisine;
		this.calorieCount = calorieCount;
		this.fatCount = fatCount;
		this.sugarCount = sugarCount;
		this.proteinCount = proteinCount;
		this.URL = URL;
	}

	public int getRecipeID() {
		return this.recipeID;
	}
	public String getRecipeName() {
		return this.recipeName;
	}
	public String getCuisine() {
		return this.cuisine;
	}
	public double getCalorieCount() {
		return this.calorieCount;
	}
	public double getFatCount() {
		return this.fatCount;
	}
	public double getSugarCount() {
		return this.sugarCount;
	}
	public double getProteinCount() {
		return this.proteinCount;
	}
	public String getURL() {
		return this.URL;
	}

	public void setRecipeID(int id) {
		this.recipeID = id;
	}
	public void setRecipeName(String name) {
		this.recipeName = name;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public void setCalorieCount(double count) {
		this.calorieCount = count;
	}
	public void setFatCount(double count) {
		this.fatCount = count;
	}
	public void setSugarCount(double count) {
		this.sugarCount = count;
	}
	public void setProteinCount(double count) {
		this.proteinCount = count;
	}
	public void setURL(String url) {
		this.URL = url;
	}
}
