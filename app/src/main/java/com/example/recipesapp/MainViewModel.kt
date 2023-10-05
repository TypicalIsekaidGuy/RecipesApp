package com.example.recipesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val servings = mutableStateOf(1)
    private val _totalPrepTime = 5
    private val totalPrepTime = mutableStateOf(_totalPrepTime)
    var strTotalPrepTime by mutableStateOf(totalPrepTime.value.minsToString())
    private val _totalCookTime = 20
    private val totalCookTime = mutableStateOf(_totalCookTime)
    var strTotalCookTime by mutableStateOf(totalCookTime.value.minsToString())

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

}