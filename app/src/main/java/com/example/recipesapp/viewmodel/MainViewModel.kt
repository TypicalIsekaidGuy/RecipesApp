package com.example.recipesapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.recipesapp.AuthRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.screens.minsToString
import com.example.recipesapp.model.Ingredient

class MainViewModel(val authRepository: AuthRepository): ViewModel() {
    var currentRecipe by authRepository.currentRecipe
    val servings = mutableStateOf(1)
    private val _totalPrepTime = currentRecipe.prepareTime - 10
    private val totalPrepTime = mutableStateOf(_totalPrepTime)
    var strTotalPrepTime by mutableStateOf(totalPrepTime.value.minsToString())
    private val _totalCookTime = currentRecipe.prepareTime
    private val totalCookTime = mutableStateOf(_totalCookTime)
    var strTotalCookTime by mutableStateOf(totalCookTime.value.minsToString())
    var ingredientText by mutableStateOf(getIngredients())
    val isFavorite: MutableState<Boolean>
        get() =mutableStateOf(authRepository.isCurrentRecipeFavorite(currentRecipe))

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
    fun addFavorite(recipe: Recipe){
        authRepository.addCurrentRecipeToFavorite(recipe)
    }
    fun removeFavorite(recipe: Recipe){
        authRepository.removeCurrentRecipeFromFavorite(recipe)

    }

}
