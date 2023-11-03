package com.example.recipesapp

data class SortElement(
    val name: String,
    val sortBy: Sort,
    val onClick: () -> Unit
)
enum class Sort{
    ALL,
    MAIN_COURSE,
    BREAKFAST,
    SALAD
}