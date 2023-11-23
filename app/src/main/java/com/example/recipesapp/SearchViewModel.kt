package com.example.recipesapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(val authRepository: AuthRepository): ViewModel() {
    val TAG = "SearchViewModel"

/*    init {
        Log.d(TAG,authRepository.TAG)
        get_data()
    }*/
private val _data: MutableState<List<Recipe>> = mutableStateOf(authRepository.recipess)
    val data: MutableState<List<Recipe>> = mutableStateOf(_data.value)
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
            data.value = _data.value.toList() as MutableList<Recipe>
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
        if(meal=="All"){
            data.value = _data.value.toList() as MutableList<Recipe>
            return
        }
        data.value =
            _data.value.toList() as MutableList<Recipe>  // Create a new List with the same data
        val sortedRecipes = data.value.filter { it.meal.toLowerCase() == meal.toLowerCase() }
        data.value = sortedRecipes as MutableList<Recipe>

        Log.d(TAG,_data.toString())
        Log.d(TAG,data.toString())
    }

/*    fun loadRecipes(){
        Log.d("Auth1",_data.value.isEmpty().toString())
        Log.d("Auth2",authRepository.recipess.toString())
        Log.d("Auth3",authRepository.recipess.toString())
        if(_data.value.isEmpty()){
            _data.value = authRepository.recipess.toMutableList()
            data.value = _data.value
            val meals = _data.value.map { it.meal }.distinct()

            sort_list = meals.mapIndexed { index, meal ->
                SortElement(meal, index+1) {
                    sortRecipes(meal)
                }
            } as MutableList<SortElement>
        }
    }*/
    private fun loadUpFromData(){
        val meals = _data.value.map { it.meal }.distinct()

        sort_list = meals.mapIndexed { index, meal ->
            SortElement(meal, index+1) {
                sortRecipes(meal)
            }
        } as MutableList<SortElement>}




/*    private fun get_data() {
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
                        SortElement(meal, index+1) {
                            sortRecipes(meal)
                        }
                    } as MutableList<SortElement>
*//*
                    sort_list.add(SortElement("All",0){sortRecipes("All")})
*//*
                }
                Log.d("Zalll", _data.value[0].description)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
    fun setCurrentRecipe(recipe: Recipe){

        authRepository.setCurrentRecipe(recipe)
        authRepository.addCurrentRecipeToSeen(recipe)

    }
    fun setRecipes(){
        Log.d(TAG,authRepository.recipess.toString())
        _data.value = authRepository.recipess
        data.value = _data.value
        loadUpFromData()
    }

}