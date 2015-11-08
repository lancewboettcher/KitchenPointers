package com.kitchenpointers.domain;

import java.util.ArrayList;

public class SearchCriteria {
	private ArrayList<String> includedIngredients;
	private ArrayList<String> excludedIngredients;
	private String cuisine;
	private int calories;
	private String sortBy;
	
	public ArrayList<String> getIncludedIngredients() {
		return includedIngredients;
	}
	public void setIncludedIngredients(ArrayList<String> includedIngredients) {
		this.includedIngredients = includedIngredients;
	}
	public ArrayList<String> getExcludedIngredients() {
		return excludedIngredients;
	}
	public void setExcludedIngredients(ArrayList<String> excludedIngredients) {
		this.excludedIngredients = excludedIngredients;
	}
	public String getCuisine() {
		return cuisine;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		this.calories = calories;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
}
