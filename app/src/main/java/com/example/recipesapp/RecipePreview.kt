package com.example.recipesapp

import androidx.compose.ui.graphics.ImageBitmap

data class RecipePreview(
    val name: String,
    val image: ImageBitmap,
    val prepareTime: Int,
    val views :Int,
    var isFavorite: Boolean
)
