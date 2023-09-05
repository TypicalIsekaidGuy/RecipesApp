package com.example.recipesapp

sealed class Screen(val route: String){
    object MainScreen: Screen("main_screen")
    object SearchScreen: Screen("search_screen")
    object FavoriteScreen: Screen("calculator_screen")
    object UserScreen: Screen("user_screen")
    object CreateScreen: Screen("create_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}
