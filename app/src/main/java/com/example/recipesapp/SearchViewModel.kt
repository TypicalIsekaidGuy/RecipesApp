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
    var data: MutableStateFlow<List<Recipe>> =  MutableStateFlow(_data.value.toList())
    private var _originalData: List<Recipe> = emptyList()
    var sort_list : MutableList<SortElement> = mutableListOf()


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    fun onSearchChange(text: String){
        try{

            _searchText.value= text
            Log.d(TAG,_searchText.value)
            data.value = _data.value.toList()
            val filteredData = if (_searchText.value.isBlank()) {
                // If the search text is empty, show all data
                data.value.toList()
            } else {
                data.value.filter { currency ->
                    currency.name.contains(_searchText.value, ignoreCase = true)
                }
            }
            data.value = filteredData as MutableList<Recipe>
        }
        catch (e: Exception){
            e.message?.let { Log.d(TAG, it) }
        }
    }

    fun load(list : List<Recipe>){
        authRepository.saveRecipeToDatabase(list)
    }
    // Function to filter the data based on meal.
    fun sortRecipes(meal: String) {
        data.value = _data.value.toList()  // Create a new List with the same data
        val sortedRecipes = data.value.filter { it.meal.toLowerCase() == meal.toLowerCase() }
        data.value = sortedRecipes

        Log.d(TAG,_data.toString())
        Log.d(TAG,data.toString())
    }


    private fun get_data() {
        viewModelScope.launch {
            try {
                authRepository.getRecipesFromDatabase().collect {
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
    }

}