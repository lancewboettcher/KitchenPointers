package com.kitchenpointers.dao;

public class IngredientOntologicalRelationship {
	private String NDB_No;
	private String ingredientName;
	private String isACategoryNumber;
	private String isACategoryName;
	
	public IngredientOntologicalRelationship(String NDB_No, String ingredientName, String isACategoryNumber, String isACategoryName) {
		this.NDB_No = NDB_No;
		this.ingredientName = ingredientName;
		this.isACategoryNumber = isACategoryNumber;
		this.isACategoryName = isACategoryNumber;
	}
}
