package com.example.recipesapp

data class Ingredient (
    val id: Int,
    val name: String,
    var quantity: Float,
    var isWeightable: Boolean
        )
