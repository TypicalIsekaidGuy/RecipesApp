package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(val authRepository: AuthRepository): ViewModel() {
    private var _data = MutableStateFlow<List<Recipe>>(emptyList())
    var data: MutableStateFlow<List<Recipe>> =  MutableStateFlow(_data.value.toList())
    init {
        viewModelScope.launch {
            authRepository.getFavorites().collect { favorites ->
                _data.value = favorites
                Log.d("ZAAuth", _data.value.toString())
            }
        }
    }

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