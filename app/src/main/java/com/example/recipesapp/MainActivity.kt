package com.example.recipesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.recipesapp.model.Screen
import com.example.recipesapp.ui.screens.Navigation
import com.example.recipesapp.ui.theme.RecipesAppTheme
import com.example.recipesapp.viewmodel.FavoriteViewModel
import com.example.recipesapp.viewmodel.MainViewModel
import com.example.recipesapp.viewmodel.SearchViewModel
import com.example.recipesapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var viewModelMap: HashMap<Screen, ViewModel> = HashMap()
    private val authRepository: AuthRepository = AuthRepository()
    class UserViewModelFactory(private val authRepository: AuthRepository,
                               private val setRecipes: () -> Unit) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(authRepository,setRecipes) as T
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
        viewModelMap[Screen.FavoriteScreen] = ViewModelProvider(this,FavoriteViewModelFactory(authRepository))[FavoriteViewModel::class.java]
        viewModelMap[Screen.SearchScreen] = ViewModelProvider(this,SearchViewModelFactory(authRepository))[SearchViewModel::class.java]
        viewModelMap[Screen.MainScreen] = ViewModelProvider(this,MainViewModelFactory(authRepository))[MainViewModel::class.java]
        viewModelMap[Screen.UserScreen] = ViewModelProvider(this,UserViewModelFactory(authRepository,
            { (viewModelMap[Screen.SearchScreen] as SearchViewModel).setRecipes() }))[UserViewModel::class.java]
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                (viewModelMap[Screen.UserScreen] as UserViewModel).message.collect { message ->
                    message?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                (viewModelMap[Screen.UserScreen] as UserViewModel).isLoading.value
            }
        }
        setContent {
            RecipesAppTheme {
                Navigation(viewModels = viewModelMap)


            }

        }
    }
    override fun onStop() {
        super.onStop()

        authRepository.updateFavoritesInDatabase()
        authRepository.updateSeenInDatabase()
    }
    override fun onDestroy() {
        super.onDestroy()

        authRepository.updateFavoritesInDatabase()
        authRepository.updateSeenInDatabase()
    }
}
