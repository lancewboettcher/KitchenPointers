package com.kitchenpointers.service;

import java.util.ArrayList;

import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;

public interface RecipeService {
	
	public ArrayList<Recipe> getRecipes(SearchCriteria criteria);

	public void addRecipe(Recipe recipe);

	void deleteRecipe(int recipeId);

}
