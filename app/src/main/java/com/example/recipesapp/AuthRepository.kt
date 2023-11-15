package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    var TAG = "AuthRepository"
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val recipes = mutableListOf<Recipe>()
    var recipess = listOf<Recipe>()
    var favorites = mutableListOf<Recipe>()
    private val firebaseData = Firebase.database("https://recipiesapp-b482b-default-rtdb.europe-west1.firebasedatabase.app/").reference
    val list_ingredients1 = listOf<Ingredient>(
        Ingredient("Lettuce".hashCode(), "Lettuce", 2.0f, true),)

    var currentRecipe = mutableStateOf(Recipe(
        "Vegan Mix Vegetable Caesar".hashCode(),
        "Vegan Mix Vegetable Caesar",
        ImageBitmap(20,20),
        20, // Prepare time set to 20 minutes
        1,
        "Instructions for Vegan Mix Vegetable Caesar",
        list_ingredients1,
        "1. Wash and chop lettuce, cucumbers, tomatoes, bell peppers, and red onions.\n" +
                "2. In a bowl, mix olive oil, vinegar, salt, and pepper to make the dressing.\n" +
                "3. Toss the chopped vegetables with the dressing and serve."
    ))

    fun logout() = auth.signOut()

    private fun currentUser() = auth.currentUser

    fun checkUser(): Boolean{
        Log.d(TAG,currentUser().toString())
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
            recipeMap["presentation"] = recipe.presentation

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

    fun getRecipesFromDatabase(isLoading: MutableState<Boolean>): Flow<List<Recipe>> = flow {

        Log.d("AuthRepo","stated repo")
        val recipes = mutableListOf<Recipe>()

        try {

            Log.d("AuthRepo","stated repo")
            val dataSnapshot = firebaseData.child("recipes").get().await()
            if (dataSnapshot.exists()) {
                for (children in dataSnapshot.children) {

                    Log.d("AuthRepo","stated repo")
                    val recipeMap = children.value as Map<String, Any>

                    // Extract data from the recipeMap
                    val id = recipeMap["id"] as Long
                    val name = recipeMap["name"] as String
                    val image = convertBase64ToImageBitmap(recipeMap["image"] as String)
                    val prepareTime = recipeMap["prepareTime"] as Long
                    val views = recipeMap["views"] as Long
                    val meal = recipeMap["meal"] as String
                    val description = recipeMap["description"] as String
                    val presentation = recipeMap["presentation"] as String
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
                        id.toInt(), name, image!!, prepareTime.toInt(), views.toInt(), meal, ingredientsList, description, presentation
                    )
                    recipes.add(recipe)
                }
            }

            emit(recipes)
            Log.d(TAG,"done")
            Log.d(TAG,recipes.toString())
            recipess = recipes
            isLoading.value = false
/*
            Log.d("ZAAuth", recipes.toString())
*/
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
    fun saveUserToDatabase(user: User) {
        val userMap = mutableMapOf<String, Any>()
        userMap["id"] = user.id
        userMap["login"] = user.login
        userMap["password"] = user.password

        // Convert the list of favorites to a list of maps
        val favoritesList = mutableListOf<Map<String, Any>>()
        for (favorite in user.favorites) {
            val favoriteMap = mutableMapOf<String, Any>()
            favoriteMap["id"] = favorite.id
            favoriteMap["recipe_id"] = favorite.recipe_id
            favoritesList.add(favoriteMap)
        }
        userMap["favorites"] = favoritesList
        // Push the user data to the "users" node in Firebase
        try {
            firebaseData.child("users").child(user.id.toString()).updateChildren(userMap)
        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
            e.printStackTrace()
        }
    }


    fun getFavorites(): Flow<List<Recipe>> = flow {
        if (favorites.isEmpty()) {
            Log.d("ZAAuth","STARTED")
                val dataSnapshot = firebaseData.child("users").child(currentUser()?.email!!.hashCode().toString()).get().await()
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.value as Map<String, Any>
                    val favors = user["favorites"] as List<Map<String, String>>
                    val favoritesId = mutableListOf<String>()

                    Log.d("ZAAuth",
                        recipes.find { it.id==favors.first()["recipe_id"]?.toInt()}.toString()
                    )
                    for (favorite in favors) {
                        favoritesId.add(favorite["recipe_id"]!!)
                    }


                    // Now fetch recipes based on the IDs
                    val recipes = recipes.toList().filter{favoritesId.contains(it.id.toString())}
                    favorites = recipes as MutableList<Recipe>
                    emit(favorites)
                    Log.d("ZAAuth", favorites.toString())
                }


        }
    }.flowOn(Dispatchers.IO)

    fun isCurrentRecipeFavorite(recipe: Recipe):Boolean{
        Log.d(TAG,favorites.toString())
        return recipe in favorites
    }

    fun addCurrentRecipeToFavorite(recipe: Recipe) {
        val favoriteMap = mapOf(
            "id" to recipe.id,
            "recipe_id" to recipe.id
        )

        // Check if the recipe is already in favorites
        Log.d(TAG,"not yet Added")
        if (!favorites.contains(recipe)) {
            // Add the recipe to the local favorites list
            Log.d(TAG,"Added")
            favorites.add(recipe)
            Log.d(TAG,isCurrentRecipeFavorite(recipe).toString())

            // Update the user's favorites in the database
            updateUserFavoritesInDatabase(favoriteMap, true)
        }
    }

    fun removeCurrentRecipeFromFavorite(recipe: Recipe) {
        // Check if the recipe is in favorites
        if (favorites.contains(recipe)) {
            // Remove the recipe from the local favorites list
            favorites.remove(recipe)
            Log.d(TAG,"Removed")

            // Update the user's favorites in the database
            updateUserFavoritesInDatabase(mapOf("id" to recipe.id), false)
        }
    }

    private fun updateUserFavoritesInDatabase(favoriteMap: Map<String, Any>, isAdding: Boolean) {
        // Update the user's favorites in the "users" node in Firebase
/*        try {
           val userFavoritesRef = firebaseData.child("users").child(currentUser()?.id.toString()).child("favorites")

            if (isAdding) {
                // Add the new favorite to the database
                userFavoritesRef.push().setValue(favoriteMap)
            } else {
                // Find and remove the specific favorite from the database
                userFavoritesRef.orderByChild("id").equalTo(favoriteMap["id"]).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (childSnapshot in snapshot.children) {
                                childSnapshot.ref.removeValue()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(TAG, "Error removing favorite: ${error.message}")
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error updating user favorites in the database: ${e.message}")
        }*/
    }



}
