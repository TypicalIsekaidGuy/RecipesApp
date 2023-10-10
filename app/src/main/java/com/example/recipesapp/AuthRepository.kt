package com.example.recipesapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class AuthRepository() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun logout() = auth.signOut()

    private fun currentUser() = auth.currentUser

    fun checkUser(): Boolean{
        if (currentUser()!=null)
            return true
        return false
    }

    // Function to sign in with email and password
    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: () -> Unit,
        onFailure: (e: String) -> Unit
    ) {
        if(currentUser()==null){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        onResult()
                    } else {

                        val exception = task.exception
                        if (exception != null) {
                            // Handle different types of exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidCredentialsException -> "Invalid email or password${exception.message}"
                                is FirebaseAuthInvalidUserException -> "User not found ${exception.message}"
                                else -> "Authentication faileddddddd${exception.message}"
                            }
                            onFailure(errorMessage)
                        } else {
                            onFailure("Authentication failed")
                        }
                    }
                }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult()
                    } else {
                        val exception = task.exception
                        if (exception != null) {
                            // Handle different types of exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidCredentialsException -> "Invalid email or password${exception.message}"
                                is FirebaseAuthInvalidUserException -> "User not found ${exception.message}"
                                else -> "Authentication failed${exception.message}"
                            }
                            onFailure(errorMessage)
                        } else {
                            onFailure("Authentication failed")
                        }
                    }
                }
        }

    }
}
