package com.example.recipesapp

data class Ingridient (
    val id: Int,
    val name: String,
    var quantity: Float,
    var isWeightable: Boolean
        )
