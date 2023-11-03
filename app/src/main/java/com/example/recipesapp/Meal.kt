package com.example.recipesapp

data class Meal(
    val name: String,
    var recipies: MutableList<Recipe>
)