package com.kitchenpointers.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kitchenpointers.domain.GetRecipesResponse;
import com.kitchenpointers.domain.Recipe;
import com.kitchenpointers.domain.SearchCriteria;
import com.kitchenpointers.service.RecipeService;

import exception.AddRecipeException;
import exception.NoRecipesFoundException;

@RestController
@EnableAutoConfiguration
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/getRecipeById/{recipeId}", method = RequestMethod.GET)
    public Recipe getRecipeById(@PathVariable Integer recipeId) throws NoRecipesFoundException {
        System.out.println("Called get Recipe By ID: " + recipeId);
        
        Recipe recipe = recipeService.getRecipeById(recipeId);
        
        if (recipe == null) {
        	throw new NoRecipesFoundException("The requested recipeId does not exist");
        }
        else {
        	return recipe;
        }
    }
    
    @RequestMapping(value = "/getRecipes", method = RequestMethod.POST)
    public GetRecipesResponse getRecipes(@RequestBody SearchCriteria criteria) {
        System.out.println("Called get Recipes!");

        GetRecipesResponse recipes = new GetRecipesResponse();
        recipes.setRecipes(recipeService.getRecipes(criteria));
        
        if (recipes.getRecipes() == null || recipes.getRecipes().isEmpty()) {
        	throw new NoRecipesFoundException("No recipes found matching your criteria");
        }

        return recipes;
    }

    @RequestMapping(value = "/addRecipe", method = RequestMethod.POST)
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        System.out.println("Called add recipe");
        
        String checkArgumentResponse = recipeService.checkAddRecipeArguments(recipe);
        
        if (checkArgumentResponse != null) {
        	//Throw a Bad Request if not all required fields are there
        	throw new IllegalArgumentException(checkArgumentResponse);
        }

        int recipeId = recipeService.addRecipe(recipe);
        
        Recipe createdRecipe = recipeService.getRecipeById(recipeId);
        
        //See if the recipe exists after creation
    	if (createdRecipe == null) {
        	throw new AddRecipeException("Error finding recipe after creation");
        }
        
        return createdRecipe;
    }

    @RequestMapping(value = "/deleteRecipe", method = RequestMethod.DELETE)
    public String addRecipe(@RequestBody Integer recipeId) {

    	if (recipeId == null) {
    		throw new IllegalArgumentException("recipeId is required");
    	}
    	
    	System.out.println("Called delete with recipeId: " + recipeId);
    	
    	//See if the recipe exists
    	if (recipeService.getRecipeById(recipeId) == null) {
        	throw new NoRecipesFoundException("Recipe with id '" + recipeId + "' does not exist");
        }
    	
        recipeService.deleteRecipe(recipeId);

        return "Deleted recipe with ID " + recipeId;
    }
    
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
    
	@ExceptionHandler
	void handleNoRecipesFoundException(NoRecipesFoundException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.NOT_FOUND.value());
	}
	
	@ExceptionHandler
	void handleAddRecipeException(AddRecipeException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.EXPECTATION_FAILED.value());
	}
}