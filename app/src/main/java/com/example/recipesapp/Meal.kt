package com.example.recipesapp

data class Meal(
    val name: String,
    val recipies: MutableList<RecipePreview>
)