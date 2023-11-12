package com.example.recipesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var currentRecipe= Recipe(
        "Spaghetti Carbonara".hashCode(),
        "Spaghetti Carbonara",
        ImageBitmap(10,20),
        30, // Prepare time set to 30 minutes
        1,
        "Instructions for Spaghetti Carbonara",
        listOf<Ingredient>(
            Ingredient("Lettuce".hashCode(), "Lettuce", 2.0f, true),
            Ingredient("Cucumbers".hashCode(), "Cucumbers", 1.0f, true),
            Ingredient("Tomatoes".hashCode(), "Tomatoes", 2.0f, true),
            Ingredient("Bell Peppers".hashCode(), "Bell Peppers", 1.0f, true)
        ),
        "1. Cook spaghetti according to package instructions.\n" +
                "2. In a separate pan, fry bacon until crispy.\n" +
                "3. Whisk eggs and mix in grated Parmesan cheese.\n" +
                "4. Drain spaghetti and toss with egg and cheese mixture. Add bacon.\n" +
                "5. Season with salt and pepper. Serve."
    )
    val servings = mutableStateOf(1)
    private val _totalPrepTime = currentRecipe.prepareTime - 10
    private val totalPrepTime = mutableStateOf(_totalPrepTime)
    var strTotalPrepTime by mutableStateOf(totalPrepTime.value.minsToString())
    private val _totalCookTime = currentRecipe.prepareTime
    private val totalCookTime = mutableStateOf(_totalCookTime)
    var strTotalCookTime by mutableStateOf(totalCookTime.value.minsToString())
    var ingredientText by mutableStateOf(getIngredients())

    private fun getIngredients(): String {
        val ingredientsList: List<Ingredient> = // your list of ingredients

            return currentRecipe.ingridients.joinToString("\n") { ingredient ->
                val quantityText = if (ingredient.isWeightable) {
                    "${ingredient.quantity} grams"
                } else {
                    ingredient.quantity.toString()
                }

                "1. ${ingredient.name} - $quantityText"
            }
    }


    fun decreaseServings(){
        if(servings.value >1){

            servings.value--
            changeTime()
        }
    }
    fun addServings(){
        servings.value++
        changeTime()
    }
    private fun changeTime(){
        totalPrepTime.value = _totalPrepTime*servings.value
        totalCookTime.value = _totalCookTime*servings.value
        strTotalPrepTime = totalPrepTime.value.minsToString()
        strTotalCookTime = totalCookTime.value.minsToString()
    }
    private fun Int.minsToString(): String{
        when{
            this<60 -> return "$this mins"
            this == 60 -> return "1 hour"
            this in 61..119 -> return "1 hour \n${this%60} mins"
            this> 119 && this%60==0 -> return "${this/60} hours"
            this> 120 && this%60!=0 -> return "${this/60} hours \n${this%60} mins"
        }
        return ""
    }
    fun addFavorite(recipe: Recipe){
/*
        authRepository.addCurrentRecipeFromFavorite(recipe)
*/
    }
    fun removeFavorite(recipe: Recipe){
/*
        authRepository.removeCurrentRecipeFromFavorite(recipe)
*/

    }

}