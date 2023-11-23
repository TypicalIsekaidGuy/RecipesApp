package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

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