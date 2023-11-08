package com.example.recipesapp

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.google.gson.Gson


data class Recipe(
    val id: Int = 0,
    val name: String= "",
    var image: ImageBitmap,
    val prepareTime: Int = 1,
    val views :Int = 1,
    val meal: String= "",
    val ingridients: List<Ingredient> = emptyList(),
    val description: String = ""
){
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}
class RecipeArgType : JsonNavType<Recipe>() {
    override fun fromJsonParse(value: String): Recipe = Gson().fromJson(value, Recipe::class.java)

    override fun Recipe.getJsonParse(): String = Gson().toJson(this)
}