package com.kitchenpointers.dao;

import java.util.ArrayList;

import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;

public interface RecipeDao {

	public ArrayList<Recipe> getRecipes(SearchCriteria criteria);

	void addRecipe(Recipe recipe);
	
}
