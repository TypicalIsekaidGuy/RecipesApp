package com.example.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.ui.theme.RecipesAppTheme

class MainActivity : ComponentActivity() {
    var viewModelMap: HashMap<Screen, ViewModel> = HashMap()
    class UserViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(authRepository) as T
        }
    }
    class FavoriteViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel() as T
        }
    }
    class SearchViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel() as T
        }
    }
    class MainViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelMap[Screen.UserScreen] = ViewModelProvider(this,UserViewModelFactory(AuthRepository()))[UserViewModel::class.java]
        viewModelMap[Screen.FavoriteScreen] = ViewModelProvider(this,FavoriteViewModelFactory())[FavoriteViewModel::class.java]
        viewModelMap[Screen.SearchScreen] = ViewModelProvider(this,SearchViewModelFactory())[SearchViewModel::class.java]
        viewModelMap[Screen.MainScreen] = ViewModelProvider(this,MainViewModelFactory())[MainViewModel::class.java]

        setContent {
            RecipesAppTheme {
                Navigation(viewModels = viewModelMap)
            }

        }
    }
}
