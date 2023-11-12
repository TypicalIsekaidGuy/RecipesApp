package com.example.recipesapp

data class User (
    val id: Int,
    val login: String,
    val password: String,
    val favorites: List<Favorites>
)