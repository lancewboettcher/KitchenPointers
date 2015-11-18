package com.kitchenpointers.domain;

public class Ingredient {
	private int ingredientId;
	private String name;
	private double quantity;
	private String unit;
	
	public Ingredient() {
		this.ingredientId = 0;
		this.name = "";
		this.quantity = 0.0;
		this.unit = "";
	}
	
	public Ingredient(int ingredientId, String name, double quantity, String unit) {
		super();
		this.ingredientId = ingredientId;
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
	}

	public int getIngredientId() {
		return ingredientId;
	}
	public void setIngredientId(int ingredientId) {
		this.ingredientId = ingredientId;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
