package com.example.recipesapp

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(val authRepository: AuthRepository): ViewModel() {
    val TAG = "SearchViewModel"

    init {
        get_data()
    }

    private val _data = MutableStateFlow<List<Recipe>>(emptyList())
    val data: StateFlow<List<Recipe>> = _data


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    fun onSearchChange(text: String){
        try{

            _searchText.value= text
            Log.d(TAG,_searchText.value)
            val filteredData = if (_searchText.value.isBlank()) {
                // If the search text is empty, show all data
                _data.value.toList()
            } else {
                _data.value.filter { currency ->
                    currency.name.contains(_searchText.value, ignoreCase = true)
                }
            }
            _data.value = filteredData as MutableList<Recipe>
        }
        catch (e: Exception){
            e.message?.let { Log.d(TAG, it) }
        }
    }
    fun filterByMeal(meal: String){
        _data.value.filter { currency ->
            currency.meal == meal
        }
    }
    fun load(list : List<Recipe>){
        authRepository.saveRecipeToDatabase(list)
    }
    private fun get_data() {
        viewModelScope.launch {
            try {
                val recipes = authRepository.getRecipesFromDatabase()
                _data.value = recipes
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors, e.g., show an error message or log the exception.
            }
        }
    }
}