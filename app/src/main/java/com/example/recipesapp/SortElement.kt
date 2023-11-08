package com.example.recipesapp

data class SortElement(
    val name: String,
    val order: Int,
    val onClick: () -> Unit)