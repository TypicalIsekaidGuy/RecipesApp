package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class AuthRepository() {
    val TAG = "AuthRepository"
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val recipes = mutableListOf<Recipe>()
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


    fun saveRecipeToDatabase(recipes: List<Recipe>) {
        for (recipe in recipes){

            val recipeMap = mutableMapOf<String, Any>()
            recipeMap["id"] = recipe.id
            recipeMap["name"] = recipe.name
            recipeMap["prepareTime"] = recipe.prepareTime
            recipeMap["views"] = recipe.views
            recipeMap["meal"] = recipe.meal
            recipeMap["description"] = recipe.description

            // Convert the ImageBitmap to a format that can be saved to Firebase (e.g., base64)
            val imageBase64 = convertImageBitmapToBase64(recipe.image)
            recipeMap["image"] = imageBase64

            // Convert the list of ingredients to a list of maps
            var ingredientsList = mutableMapOf<String,Any>()
            for (ingredient in recipe.ingridients){
                var ingredientMap = mutableMapOf<String,Any>()
                ingredientMap["id"] = ingredient.id
                ingredientMap["name"] = ingredient.name
                ingredientMap["quantity"] = ingredient.quantity
                ingredientMap["isWeightable"] = ingredient.isWeightable
                ingredientsList[ingredient.name] = ingredientMap
            }
            recipeMap["ingredients"] = ingredientsList
            val recip = mutableMapOf<String, Any>()
            recip[recipe.name] = recipeMap
            // Push the recipe data to the "recipes" node in Firebase
            try {
                firebaseData.child("recipes").updateChildren(recip)
            }
            catch (e: Exception){
                Log.d(TAG,e.message!!)
            }
        }
    }
    fun getRecipesFromDatabase(): Flow<List<Recipe>> = flow {
        val recipes = mutableListOf<Recipe>()

        try {
            val dataSnapshot = firebaseData.child("recipes").get().await()
            if (dataSnapshot.exists()) {
                for (children in dataSnapshot.children) {
                    val recipeMap = children.value as Map<String, Any>

                    // Extract data from the recipeMap
                    val id = recipeMap["id"] as Long
                    val name = recipeMap["name"] as String
                    val image = convertBase64ToImageBitmap(recipeMap["image"] as String)
                    val prepareTime = recipeMap["prepareTime"] as Long
                    val views = recipeMap["views"] as Long
                    val meal = recipeMap["meal"] as String
                    val description = recipeMap["description"] as String
                    val ingredientsMap: Map<String, Map<String, Any>> = recipeMap["ingredients"] as Map<String, Map<String, Any>>

                    var ingredientsList = emptyList<Ingredient>()
                    if (ingredientsMap != null) {
                        ingredientsList = ingredientsMap.map { (ingredientName, ingredientData) ->
                            val id = (ingredientData["id"] as Long).toInt()
                            val name = ingredientData["name"] as String
                            val quantity = /*(ingredientData["quantity"] as Long).toFloat()*/ 0F
                            val isWeightable = ingredientData["isWeightable"] as Boolean

                            Ingredient(id, name, quantity, isWeightable)
                        }
                        // Now 'ingredientsList' contains the list of Ingredient objects
                    } else {
                        Log.d(TAG, "HUYNYA")
                    }

                    // Process the data, create Recipe objects, or perform any other operations as needed
                    val recipe = Recipe(
                        id.toInt(), name, image!!, prepareTime.toInt(), views.toInt(), meal, ingredientsList, description
                    )
                    recipes.add(recipe)
                }
            }

            emit(recipes)
            Log.d("ZAAuth", recipes.toString())
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)


    // Helper function to convert ImageBitmap to base64 (implement this)
// Function to convert ImageBitmap to base64
    private fun convertImageBitmapToBase64(imgbm: ImageBitmap): String {
        val bm : Bitmap =  imgbm.asAndroidBitmap()
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)!!
    }
    private fun convertBase64ToImageBitmap(base64String: String): ImageBitmap? {

            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(imageBytes))
            return bitmap.asImageBitmap()

    }


}
