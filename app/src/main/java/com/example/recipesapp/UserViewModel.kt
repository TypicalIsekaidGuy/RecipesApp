package com.example.recipesapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class UserViewModel(private val authRepository: AuthRepository): ViewModel() {
    val TAG = "USER_VIEW_MODEL"
    val isLoginScreen =  mutableStateOf(true)
    val login =  mutableStateOf("")
    val password = mutableStateOf("")
    val name = mutableStateOf("")
    private val bannedWords = mutableListOf("fuck")
    val isUserInitialized =  mutableStateOf(false)
    private val statusMessage = MutableLiveData<Event<String>>()
    val isLoading = mutableStateOf(true)

    private val _statusMessage = MutableStateFlow<String?>(null)

    val message: StateFlow<String?>
        get() = _statusMessage


    private val _recipies = mutableListOf<String>()
    val recipies = _recipies

    init {
        Log.d(TAG,"Started")
        viewModelScope.launch {
            try {
                authRepository.getRecipesFromDatabase(isLoading).collect{}
                Log.d(TAG,authRepository.recipes.isEmpty().toString())

                isUserInitialized.value =  authRepository.checkUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
    private fun tryEnterApp(){

    }

    fun confirmAuth():Boolean{
        try{
            isLoginCorrect()
            isPasswordCorrect()
            isNameCorrect()
            val user = User(login.value.hashCode(),login.value,password.value, listOf(Favorites("Beef Tacos".hashCode().toString().hashCode(),"Beef Tacos".hashCode())))
            authRepository.saveUserToDatabase(user)
            authRepository.signInWithEmailAndPassword(login.value,password.value, { Log.d(TAG,"YEEEY")},{Log.d("Failure",it)})
            Log.d(TAG,"yes")
            return true
        }
        catch(e: Exception){
Log.d(TAG,e.message!!)
        e.printStackTrace()}
        finally {

        }
        return false
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
            throw IncorrectWordException("The name is incorrect")

    }
    inner class IncorrectWordException(message:String): Exception(message){
        init {
            statusMessage.value = Event(message)
        }
    }
}