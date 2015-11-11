package com.kitchenpointers.dao;

class RecipeIngredientsTuple {
	private int recipeID;
	private String ingredientID;
	private String quantity;

	public RecipeIngredientsTuple() {
		this.recipeID = null;
		this.ingredientID = null;
		this.quantity = null;
	}
	public RecipeIngredientsTuple(int recipeID, String ingredientID, String quantity) {
		this.recipeID = recipeID;
		this.ingredientID = ingredientID;
		this.quantity = quantity;
	}

	public int getRecipeID() {
		return this.recipeID;
	}
	public String getIngredientID() {
		return this.ingredientID;
	}
	public String getQuantity() {
		return this.quantity;
	}

	public void setRecipeID(int id) {
		this.recipeID = id;
	}
	public void setIngredientID(String id) {
		this.ingredientID = id;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
