package com.kitchenpointers.domain;

import java.util.ArrayList;

public class Recipe {
    private Integer id;
    private String name;
    private Integer calories;
    private Integer fat;
    private Integer sugar;
    private Integer protein;
    private String cuisine;
    private Integer rating;
    private float matchScore;
    private String url;
    ArrayList<Ingredient> ingredients;

    public Recipe() {
        this.id = 0;
        this.name = "";
        this.calories = 0;
        this.fat = 0;
        this.sugar = 0;
        this.protein = 0;
        this.cuisine = "";
        this.rating = 0;
        this.matchScore = 0.0f;
        this.url = "";
        ingredients = new ArrayList<Ingredient>();
    }

    public Recipe(Integer id, String name, Integer calories, Integer fat, Integer sugar, Integer protein, String cuisine, Integer rating, float matchScore,
            String url, ArrayList<Ingredient> ingredients) {
        super();
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.sugar = sugar;
        this.protein = protein;
        this.cuisine = cuisine;
        this.rating = rating;
        this.matchScore = matchScore;
        this.url = url;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getSugar() {
        return sugar;
    }

    public void setSugar(Integer sugar) {
        this.sugar = sugar;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public float getMatchScore() {
        return matchScore;
    }
    
    public void setMatchScore(float matchScore) {
        this.matchScore = matchScore;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
