package com.example.recipesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel

class MainViewModel(private val authRepository: AuthRepository): ViewModel() {
    var currentRecipe by authRepository.currentRecipe
    var currentName = currentRecipe.name
    val servings = mutableStateOf(1)
    private val _totalPrepTime = currentRecipe.prepareTime - 10
    private val totalPrepTime = mutableStateOf(_totalPrepTime)
    var strTotalPrepTime by mutableStateOf(totalPrepTime.value.minsToString())
    private val _totalCookTime = currentRecipe.prepareTime
    private val totalCookTime = mutableStateOf(_totalCookTime)
    var strTotalCookTime by mutableStateOf(totalCookTime.value.minsToString())
    var ingredientText by mutableStateOf(getIngredients())
    var isFavorite = mutableStateOf(authRepository.isCurrentRecipeFavorite(currentRecipe))

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
        authRepository.addCurrentRecipeToFavorite(recipe)
    }
    fun removeFavorite(recipe: Recipe){
        authRepository.removeCurrentRecipeFromFavorite(recipe)
    }
    fun checkRecipe(){
        if(currentName!=currentRecipe.name){
            currentName = currentRecipe.name
            isFavorite.value = authRepository.isCurrentRecipeFavorite(currentRecipe)
        }
    }

}