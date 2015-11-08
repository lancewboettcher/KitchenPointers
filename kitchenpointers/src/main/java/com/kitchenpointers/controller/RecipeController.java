package com.kitchenpointers.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ArrayList<Recipe> getRecipes(@RequestBody SearchCriteria criteria) {
    	System.out.println("Called get Recipes");
    	System.out.println(criteria.getCalories());
    	
        return recipeService.getRecipes(criteria);
    }
    
    @RequestMapping(value = "/addRecipe", method = RequestMethod.POST)
    public void addRecipe(@RequestBody Recipe recipe) {
    	System.out.println("Called add recipe");
    	
        recipeService.addRecipe(recipe);
    }

}