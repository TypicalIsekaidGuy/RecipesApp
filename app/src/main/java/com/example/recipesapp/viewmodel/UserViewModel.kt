package com.example.recipesapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.AuthRepository
import com.example.recipesapp.model.Favorites
import com.example.recipesapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val authRepository: AuthRepository, private val setRecipes: () -> Unit): ViewModel() {
    val TAG = "USER_VIEW_MODEL"
    val isLoginScreen =  mutableStateOf(true)
    val login =  mutableStateOf("")
    val password = mutableStateOf("")
    val name = mutableStateOf("")
    private val bannedWords = mutableListOf("fuck")
    val isUserInitialized =  mutableStateOf(false)
    val isLoading =  mutableStateOf(true)
    val isLoadingFavorites =  mutableStateOf(false)
    private val _statusMessage = MutableStateFlow<String?>(null)
    val message: StateFlow<String?>
        get() = _statusMessage

    private val _recipies = mutableListOf<String>()
    val recipies = _recipies

    init {
        /*authRepository.getFavorites()*/
        viewModelScope.launch {
            try {
                Log.d(TAG,"Started")
                authRepository.getRecipesFromDatabase()

                Log.d(TAG,"Fetched")
                isUserInitialized.value =  authRepository.checkUser()//make splashScreen and check it there
                Log.d(TAG,authRepository.recipess.toString())
                setRecipes()
                Log.d(TAG,"Stopped")
                isLoading.value = false
            }
            catch (e:Exception){
                Log.e(TAG,"error",e)
            }
        }
    }
    private fun tryEnterApp(){

    }
    fun loadFavorites(){
        viewModelScope.launch {
            try {

                authRepository.getFavorites()
            }
            catch (e:Exception){
                Log.e(TAG,"error",e)
            }
        }
    }

    fun confirmAuth(){
        try{
            authRepository.TAG = "AUTH"
            isLoginCorrect()
            isPasswordCorrect()
            isNameCorrect()
            val user = User(login.value.hashCode(),login.value,password.value, listOf(Favorites("Beef Tacos".hashCode().toString().hashCode(),"Beef Tacos".hashCode())))
            authRepository.saveUserToDatabase(user)
            /*authRepository.getFavorites()*/
            authRepository.signInWithEmailAndPassword(login.value,password.value, { Log.d(TAG,"YEEEY")},{Log.d("Failure",it)})
        }
        catch(e: Exception){

        }
        finally {

        }
    }
    private fun isLoginCorrect(){
        if(!("@"  in login.value &&"." in login.value))
            throw IncorrectWordException("The mail is nonexistent")

    }
    private fun isPasswordCorrect(){
        if(password.value.length <4||password.value.lowercase() in bannedWords)
            throw IncorrectWordException("The password is incorrect")
    }
    private fun isNameCorrect(){
        if(name.value.lowercase() in bannedWords)
            throw IncorrectWordException("The password is incorrect")

    }
    inner class IncorrectWordException(message:String): Exception(message){
        init {
            _statusMessage.value =message
        }
    }
}
