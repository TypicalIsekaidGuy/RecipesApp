package com.example.recipesapp

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream


class AuthRepository() {
    val TAG = "AuthRepository"
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseData = Firebase.database("https://recipiesapp-b482b-default-rtdb.europe-west1.firebasedatabase.app/").reference

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
    val db = Firebase.database
    val ref = db.getReference("server/saving-data/fireblog/posts")

/*    fun load_from_db(){
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Process the data here
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    println(post)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }*/

    fun load_to_db(recipe: Recipe) {
        saveRecipeToDatabase(recipe)
        Log.d(TAG, firebaseData.child("sss").root.toString())
    }
    fun saveRecipeToDatabase(recipe: Recipe) {
        val recipeMap = mutableMapOf<String, Any>()
        recipeMap["id"] = recipe.id

        recipeMap["uuid"] = recipe.uuid
        recipeMap["name"] = recipe.name
        recipeMap["prepareTime"] = recipe.prepareTime
        recipeMap["views"] = recipe.views
        recipeMap["meal"] = recipe.meal

        // Convert the ImageBitmap to a format that can be saved to Firebase (e.g., base64)
        val imageBase64 = convertImageBitmapToBase64(recipe.image)
/*        recipeMap["image"] = imageBase64*/

        // Convert the list of ingredients to a list of maps
        val ingredientsList = recipe.ingridients.map { ingredient ->
            val ingredientMap = mutableMapOf<String, Any>()
            ingredientMap["id"] = ingredient.id
            ingredientMap["name"] = ingredient.name
            ingredientMap["quantity"] = ingredient.quantity
            ingredientMap["isWeightable"] = ingredient.isWeightable
            ingredientMap
        }
/*
        recipeMap["ingredients"] = ingredientsList
*/

        recipeMap["id"] = recipe.id
        val recip = mutableMapOf<String, Any>()
        recip[recipe.name] = recipeMap
        // Push the recipe data to the "recipes" node in Firebase
        try {
                firebaseData.child("recipes").setValue(recip)

        }
        catch (e: Exception){
            Log.d(TAG,e.message!!)
        }
    }

    // Helper function to convert ImageBitmap to base64 (implement this)
// Function to convert ImageBitmap to base64
    private fun convertImageBitmapToBase64(imgbm: ImageBitmap): String {
        val bm : Bitmap =  imgbm.asAndroidBitmap()
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)!!
    }


}
