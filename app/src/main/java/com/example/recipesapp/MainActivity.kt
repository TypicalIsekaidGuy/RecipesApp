package com.example.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.ui.theme.RecipesAppTheme
import com.example.recipesapp.ui.theme.Typography
import com.example.recipesapp.ui.theme.firaSansFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    var viewModelMap: HashMap<Screen, ViewModel> = HashMap()
    class UserViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel() as T
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelMap[Screen.UserScreen] = ViewModelProvider(this,UserViewModelFactory())[UserViewModel::class.java]
        viewModelMap[Screen.FavoriteScreen] = ViewModelProvider(this,FavoriteViewModelFactory())[FavoriteViewModel::class.java]
        viewModelMap[Screen.SearchScreen] = ViewModelProvider(this,SearchViewModelFactory())[SearchViewModel::class.java]

        setContent {
            RecipesAppTheme {
                Navigation(viewModels = viewModelMap)
            }

        }
    }
}
