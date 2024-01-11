package com.example.recipesapp.model

data class User (
    val id: Int,
    val login: String,
    val password: String,
    val favorites: List<Favorites>
)