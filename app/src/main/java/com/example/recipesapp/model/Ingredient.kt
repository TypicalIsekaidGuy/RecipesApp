package com.example.recipesapp.model

data class Ingredient (
    val id: Int,
    val name: String,
    var quantity: Float,
    var isWeightable: Boolean
        )
