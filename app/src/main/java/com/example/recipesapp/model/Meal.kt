package com.example.recipesapp.model

data class Meal(
    val name: String,
    var recipies: MutableList<Recipe>
)