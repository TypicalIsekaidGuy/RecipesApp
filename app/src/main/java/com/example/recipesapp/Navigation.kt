package com.example.recipesapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson

@Composable
fun Navigation(viewModels: HashMap<Screen, ViewModel>){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.UserScreen.route/*+ "/name/10F" */){//try using from userviewmodel if statement
        composable(route = Screen.FavoriteScreen.route){
            FavoriteScreen(navController, viewModels[Screen.FavoriteScreen] as FavoriteViewModel)
        }
        composable(route = Screen.UserScreen.route){
            UserScreen(navController,viewModels[Screen.UserScreen] as UserViewModel)
        }
        composable(route = Screen.SearchScreen.route){
            SearchScreen(navController,viewModels[Screen.SearchScreen] as SearchViewModel)
        }
        composable(route = Screen.MainScreen.route)
         {
            MainScreen(navController, viewModels[Screen.MainScreen] as MainViewModel)
        }

/*        composable(route = Screen.CalculatorScreen.route + "/{name}/{price}", arguments = listOf(navArgument("name") { type = NavType.StringType },navArgument("price") { type = NavType.FloatType } )){backStackEntry ->
            if(backStackEntry.arguments?.getString("name").isNullOrEmpty()||backStackEntry.arguments?.getFloat("price")==null){
                throw NullPointerException("NULL NAME||PRICE")
            }
            val name = backStackEntry.arguments?.getString("name") as String
            val price = backStackEntry.arguments?.getFloat("price") as Float
            CalculatorScreen(navController =  navController, viewModel, name, price)
        }*/
    }
}

fun Int.viewsToString(): String {
    return when {
        this == 1 -> "1 view"
        this in 2..100 -> "$this views"
        this in 101..999 -> "${this / 10 * 10} views"
        this in 1000..999999 -> "${this / 1000}k views"
        else -> "${this / 1000000}M views"
    }
}

fun Int.minsToString(): String{
    when{
        this<60 -> return "$this mins"
        this == 60 -> return "1 hour"
        this in 61..119 -> return "1 hour \n${this%60} mins"
        this> 119 && this%60==0 -> return "${this/60} hours"
        this> 120 && this%60!=0 -> return "${this/60} hours \n${this%60} mins"
    }
    return ""
}