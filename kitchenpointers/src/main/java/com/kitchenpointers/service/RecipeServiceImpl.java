package com.kitchenpointers.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kitchenpointers.dao.RecipeDao;
import com.kitchenpointers.dao.RecipeDaoImpl;
import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;

@Service
public class RecipeServiceImpl implements RecipeService {

	public RecipeServiceImpl() {
		
	}
	
	@Override
	public ArrayList<Recipe> getRecipes(SearchCriteria criteria) {			
		RecipeDao recipeDao = new RecipeDaoImpl();
						
		return recipeDao.getRecipes(criteria);
	}
	
	@Override
	public Integer addRecipe(Recipe recipe) {
		RecipeDao recipeDao = new RecipeDaoImpl();
		
		return recipeDao.addRecipe(recipe);
	}
	
	@Override
	public void deleteRecipe(int recipeId) {
		RecipeDao recipeDao = new RecipeDaoImpl();
		
		recipeDao.deleteRecipe(recipeId);
	}

	@Override
	public Recipe getRecipeById(int recipeId) {
		RecipeDao recipeDao = new RecipeDaoImpl();
		
		return recipeDao.getRecipeById(recipeId);
	}
	
	/* 
	 * Checks the recipe for all required fields
	 * returns null if all required fields are there
	 * and not null
	 */
	@Override
	public String checkAddRecipeArguments(Recipe recipe) {
		if (recipe.getName() == null || recipe.getName().isEmpty())
			return "addRecipe: name is required";
		if (recipe.getCuisine() == null || recipe.getCuisine().isEmpty())
			return "addRecipe: cuisine is required";
		if (recipe.getUrl() == null || recipe.getUrl().isEmpty())
			return "addRecipe: url is required";
		if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty())
			return "addRecipe: ingredients are required";
		
		return null;
	}
}
