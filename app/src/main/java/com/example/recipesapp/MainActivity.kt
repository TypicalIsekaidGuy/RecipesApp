package com.example.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.ui.theme.RecipesAppTheme

class MainActivity : ComponentActivity() {
    var viewModelMap: HashMap<Screen, ViewModel> = HashMap()
    private val authRepository: AuthRepository = AuthRepository()
    class UserViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(authRepository) as T
        }
    }
    class FavoriteViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(authRepository) as T
        }
    }
    class SearchViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(authRepository) as T
        }
    }
    class MainViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(authRepository) as T
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {//if there are problems with back button override it here
        super.onCreate(savedInstanceState)
        viewModelMap[Screen.UserScreen] = ViewModelProvider(this,UserViewModelFactory(authRepository))[UserViewModel::class.java]
        viewModelMap[Screen.FavoriteScreen] = ViewModelProvider(this,FavoriteViewModelFactory(authRepository))[FavoriteViewModel::class.java]
        viewModelMap[Screen.SearchScreen] = ViewModelProvider(this,SearchViewModelFactory(authRepository))[SearchViewModel::class.java]
        viewModelMap[Screen.MainScreen] = ViewModelProvider(this,MainViewModelFactory(authRepository))[MainViewModel::class.java]

        setContent {
            RecipesAppTheme {
                Navigation(viewModels = viewModelMap)
            }

        }
    }
}
