package com.example.recipesapp

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(val authRepository: AuthRepository): ViewModel() {
    val TAG = "SearchViewModel"

    private val list_ingridients = listOf<Ingridient>(
        Ingridient("pepper".hashCode(),"pepper",0.5f,true),
        Ingridient("peppe".hashCode(),"peppe",0.5f,true),
        Ingridient("pepp".hashCode(),"pepp",0.5f,true)
    )
    var imageBitmap = ImageBitmap(20,20)
    var _data = MutableStateFlow(
        mutableListOf(
            Recipe("Vegan Mix Vegetable Ceaser".hashCode(),"","Vegan Mix Vegetable Ceaser",imageBitmap,20,140, "salad",list_ingridients),
            Recipe("Vegan Mix Vegetable ".hashCode(),"","Vegan Mix Vegetable ",imageBitmap,20,140, "salad",list_ingridients),
            Recipe("Vegan Mix  Ceaser".hashCode(),"","Vegan Mix  Ceaser",imageBitmap,20,140, "salad",list_ingridients),
        ))

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
        authRepository.load_to_db(list[0])
    }
}