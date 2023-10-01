package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val servings = mutableStateOf(1)
    private val _totalPrepTime = mutableStateOf(5)
    private val _totalCookTime = mutableStateOf(20)

    fun decreaseServings(){
        if(servings.value >0)
            servings.value--
    }
    fun addServings(){
        servings.value++
    }
}