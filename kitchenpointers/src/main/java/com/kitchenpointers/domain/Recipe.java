package com.kitchenpointers.domain;

import java.util.ArrayList;

public class Recipe {
	private int id;
	private String name;
	private int calories;
	private int fat;
	private int sugar;
	private int protein;
	private String cuisine;
	private int rating;
	private String url;
	ArrayList<Ingredient> ingredients;
	
	public Recipe() {
		this.id = 0;
		this.name = "";
		this.calories = 0;
		this.fat = 0;
		this.sugar = 0;
		this.protein = 0;
		this.cuisine = "";
		this.rating = 0;
		this.url = "";
		ingredients = new ArrayList<Ingredient>();
	}
	
	public Recipe(int id, String name, int calories, int fat, int sugar, int protein, String cuisine, int rating,
			String url, ArrayList<Ingredient> ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.calories = calories;
		this.fat = fat;
		this.sugar = sugar;
		this.protein = protein;
		this.cuisine = cuisine;
		this.rating = rating;
		this.url = url;
		this.ingredients = ingredients;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		this.calories = calories;
	}
	public int getFat() {
		return fat;
	}
	public void setFat(int fat) {
		this.fat = fat;
	}
	public int getSugar() {
		return sugar;
	}
	public void setSugar(int sugar) {
		this.sugar = sugar;
	}
	public int getProtein() {
		return protein;
	}
	public void setProtein(int protein) {
		this.protein = protein;
	}
	public String getCuisine() {
		return cuisine;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	} 
}
