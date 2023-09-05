package com.example.recipesapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInModule {
    private var auth: FirebaseAuth = Firebase.auth

    fun onStart(){

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadLoginScreen()
        }else{
            loadMainScreen()
        }
    }

    private fun loadMainScreen() {
        TODO("Not yet implemented")
    }

    private fun loadLoginScreen() {
        TODO("Not yet implemented")
    }

}