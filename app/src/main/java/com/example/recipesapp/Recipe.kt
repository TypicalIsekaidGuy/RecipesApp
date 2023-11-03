package com.example.recipesapp

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource



data class Recipe(
    val id: Int = 0,
    var uuid: String = "",
    val name: String= "",
    var image: ImageBitmap,
    val prepareTime: Int = 1,
    val views :Int = 0,
    val meal: String= "",
    val ingridients: List<Ingridient> = emptyList()
)
