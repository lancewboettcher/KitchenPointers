package com.kitchenpointers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kitchenpointers.domain.GetRecipesResponse;
import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;
import com.kitchenpointers.service.RecipeService;

@RestController
@EnableAutoConfiguration
public class RecipeController {
	
	@Autowired
	private RecipeService recipeService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
    	System.out.println("SUP");
        return "hellosasd";
    }
    
    @RequestMapping(value = "/getRecipes", method = RequestMethod.POST)
    public GetRecipesResponse getRecipes(@RequestBody SearchCriteria criteria) {
    	System.out.println("Called get Recipes!");
    	System.out.println(criteria.getCalories());
    	
    	GetRecipesResponse recipes = new GetRecipesResponse();
    	recipes.setRecipes(recipeService.getRecipes(criteria));
    	
        return recipes;
    }
    
    @RequestMapping(value = "/addRecipe", method = RequestMethod.POST)
    public Recipe addRecipe(@RequestBody Recipe recipe) {
    	System.out.println("Called add recipe");
    	
        recipeService.addRecipe(recipe);
        
        return recipe;
    }
    
    @RequestMapping(value = "/deleteRecipe", method = RequestMethod.POST)
    public int addRecipe(@RequestBody Integer recipeId) {
    	System.out.println("Called delete recipeId: " + recipeId);
    	
        recipeService.deleteRecipe(recipeId);
        
        return recipeId;
    }

}