package com.kitchenpointers.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kitchenpointers.domain.Ingredient;
import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;

@Service
public class RecipeServiceImpl implements RecipeService {

	public RecipeServiceImpl() {
		
	}
	
	@Override
	public ArrayList<Recipe> getRecipes(SearchCriteria criteria) {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		
		Ingredient ing1 = new Ingredient();
		ing1.setIngredientId(0);
		ing1.setName("Chicken");
		ing1.setQuantity(4);
		ing1.setUnit("Pounds");
		ingredients.add(ing1);
		
		Ingredient ing2 = new Ingredient();
		ing2.setIngredientId(1);
		ing2.setName("Orange");
		ing2.setQuantity(2);
		ing2.setUnit("Cups");
		ingredients.add(ing2);
		
		Recipe firstRecipe = new Recipe();
		firstRecipe.setCalories(20);
		firstRecipe.setCuisine("Mexican");
		firstRecipe.setIngredients(ingredients);
		firstRecipe.setRating(3);
		firstRecipe.setRecipeId(3);
		
		recipes.add(firstRecipe);
		
		return recipes;
	}
	
	@Override
	public void addRecipe(Recipe recipe) {
		
	}
}
