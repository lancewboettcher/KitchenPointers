package com.kitchenpointers.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kitchenpointers.dao.JDBCConnect;
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
		ing1.setIngredientId(357);
		ing1.setName("Boneless Skinless Chicken Breast");
		ing1.setQuantity(4);
		ing1.setUnit("Piece");
		ingredients.add(ing1);
		
		Ingredient ing2 = new Ingredient();
		ing2.setIngredientId(1123);
		ing2.setName("Garlic Cloves");
		ing2.setQuantity(4);
		ing2.setUnit("Piece");
		ingredients.add(ing2);
		
		Ingredient ing3 = new Ingredient();
		ing3.setIngredientId(987);
		ing3.setName("Brown Sugar");
		ing3.setQuantity(4);
		ing3.setUnit("Tablespoon");
		ingredients.add(ing3);
		
		Ingredient ing4 = new Ingredient();
		ing4.setIngredientId(34);
		ing4.setName("Olive Oil");
		ing4.setQuantity(1);
		ing4.setUnit("Tablespoon");
		ingredients.add(ing4);
		
		Recipe firstRecipe = new Recipe();
		firstRecipe.setCalories(400);
		firstRecipe.setCuisine("Asian");
		firstRecipe.setIngredients(ingredients);
		firstRecipe.setRating(3);
		firstRecipe.setId(3);
		firstRecipe.setName("Easy Garlic Chicken");
		firstRecipe.setFat(20);
		firstRecipe.setProtein(20);
		firstRecipe.setSugar(30);
		firstRecipe.setUrl("http://www.food.com/recipe/easy-garlic-chicken-5478");
		//s
		recipes.add(firstRecipe);
		
		ArrayList<Ingredient> ingredients2 = new ArrayList<Ingredient>();
		
		Ingredient ing5 = new Ingredient();
		ing5.setIngredientId(21);
		ing5.setName("Boneless Skinless Chicken Breast");
		ing5.setQuantity(1.5);
		ing5.setUnit("Piece");
		ingredients2.add(ing5);
		
		Ingredient ing6 = new Ingredient();
		ing6.setIngredientId(223);
		ing6.setName("All Purpose Flour");
		ing6.setQuantity(1);
		ing6.setUnit("Cup");
		ingredients2.add(ing6);
		
		Ingredient ing7 = new Ingredient();
		ing7.setIngredientId(998);
		ing7.setName("Baking Powder");
		ing7.setQuantity(1);
		ing7.setUnit("Teaspoon");
		ingredients2.add(ing7);
		
		Ingredient ing8 = new Ingredient();
		ing8.setIngredientId(1923);
		ing8.setName("Eggs");
		ing8.setQuantity(2);
		ing8.setUnit("Piece");
		ingredients2.add(ing8);
		
		Ingredient ing10 = new Ingredient();
		ing10.setIngredientId(349);
		ing10.setName("Beer");
		ing10.setQuantity(0.5);
		ing10.setUnit("Cup");
		ingredients2.add(ing10);
		
		Ingredient ing11 = new Ingredient();
		ing11.setIngredientId(128);
		ing11.setName("Oil");
		ing11.setQuantity(3);
		ing11.setUnit("Cup");
		ingredients2.add(ing11);
		
		Recipe recipe2 = new Recipe();
		recipe2.setCalories(500);
		recipe2.setCuisine("American ");
		recipe2.setIngredients(ingredients2);
		recipe2.setRating(4);
		recipe2.setId(23);
		recipe2.setName("Beer Battered Chicken");
		recipe2.setFat(40);
		recipe2.setProtein(30);
		recipe2.setSugar(20);
		recipe2.setUrl("http://allrecipes.com/recipe/42881/beer-battered-chicken/?internalSource=recipe%20hub&referringId=1284&referringContentType=recipe%20hub");
		
		recipes.add(recipe2);
		
		JDBCConnect connect = new JDBCConnect();
		
		//return recipes;
		return recipes;
	}
	
	@Override
	public void addRecipe(Recipe recipe) {
		
	}
}
