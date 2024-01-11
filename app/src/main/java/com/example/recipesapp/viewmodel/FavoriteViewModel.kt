package com.example.recipesapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.recipesapp.AuthRepository
import com.example.recipesapp.model.Recipe

class FavoriteViewModel(private val authRepository: AuthRepository): ViewModel() {
    var data by authRepository.favoriteMeals


    fun setCurrentRecipe(recipe: Recipe){
        authRepository.setCurrentRecipe(recipe)
    }

/*    fun getFavorites() {
        if (_data.value.isEmpty()) {
            viewModelScope.launch {
                authRepository.getFavorites().collect { favorites ->

                    // Group recipes by meal name
                    val groupedByMeal = favorites.groupBy { it.meal }

                    // Map the grouped entries to create a list of Meal objects
                    val meals = groupedByMeal.map { (mealName, recipes) ->
                        Meal(mealName, recipes.toMutableList())
                    }

                    // Update data with the new list of meals
                    _data = MutableStateFlow(meals)
                    data.value = _data.value

                    Log.d("ZAAuth", data.value.toString())
                }
            }
        }
    }*/


/*    private fun get_data() {
        viewModelScope.launch {
            try {
                authRepository.getFavorites().collect {
                    _data.value = it
                    Log.d(TAG,data.toString())
                    data.value = _data.value.toList()  // Create a new List with the same data
                    val meals = it.map { it.meal }.distinct()
                    Log.d(TAG,_data.toString())
                    Log.d(TAG,data.toString())
                    Log.d(TAG,it.toString())
                    sort_list = meals.mapIndexed { index, meal ->
                        SortElement(meal, index) {
                            sortRecipes(meal)
                        }
                    } as MutableList<SortElement>
                }
                Log.d("Zalll", _data.value[0].description)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
}