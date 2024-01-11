package com.example.recipesapp.model

data class SortElement(
    val name: String,
    val order: Int,
    val onClick: () -> Unit)