package com.example.recipesapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class UserViewModel(private val authRepository: AuthRepository): ViewModel() {
    val TAG = "USER_VIEW_MODEL"
    val isLoginScreen =  mutableStateOf(true)
    val login =  mutableStateOf("")
    val password = mutableStateOf("")
    val name = mutableStateOf("")
    private val bannedWords = mutableListOf("fuck")
    val isUserInitialized =  mutableStateOf(false)


    private val _recipies = mutableListOf<String>()
    val recipies = _recipies

    init {
        authRepository.getFavorites()
        authRepository.TAG = "AUTH"
        isUserInitialized.value =  authRepository.checkUser()//make splashScreen and check it there
        fetchAllRecipies()
    }
    private fun tryEnterApp(){

    }
    private fun fetchAllRecipies(){

    }

    fun confirmAuth(){
        try{
            authRepository.TAG = "AUTH"
            isLoginCorrect()
            isPasswordCorrect()
            isNameCorrect()
            val user = User(login.value.hashCode(),login.value,password.value, listOf(Favorites("Beef Tacos".hashCode().toString().hashCode(),"Beef Tacos".hashCode())))
            authRepository.saveUserToDatabase(user)
            authRepository.getFavorites()
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
}
class IncorrectWordException(message:String): Exception(message)