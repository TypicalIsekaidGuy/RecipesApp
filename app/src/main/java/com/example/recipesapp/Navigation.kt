package com.example.recipesapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(viewModels: HashMap<Screen, ViewModel>){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SearchScreen.route/*+ "/name/10F" */){
        composable(route = Screen.FavoriteScreen.route){
            FavoriteScreen(navController, viewModels[Screen.FavoriteScreen] as FavoriteViewModel)
        }
        composable(route = Screen.UserScreen.route){
            UserScreen(navController,viewModels[Screen.UserScreen] as UserViewModel)
        }
        composable(route = Screen.SearchScreen.route){
            SearchScreen(navController,viewModels[Screen.SearchScreen] as SearchViewModel)
        }
        composable(route = Screen.MainScreen.route){
            MainScreen(navController,viewModels[Screen.MainScreen] as MainViewModel)
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