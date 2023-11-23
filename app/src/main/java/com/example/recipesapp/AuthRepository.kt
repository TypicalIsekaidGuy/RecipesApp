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
import com.google.firebase.database.ServerValue
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
    val viewedList = mutableMapOf<String,Boolean>()
    var recipess = listOf<Recipe>()
    var favorites = mutableStateOf(mutableListOf<Recipe>())
    var favoriteMeals = mutableStateOf(mutableListOf<Meal>())
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
    suspend fun getRecipesFromDatabase(isLoading: MutableState<Boolean>){
        val recipes = mutableListOf<Recipe>()
        Log.d(TAG,"Started")
        try {
            val dataSnapshot = firebaseData.child("recipes").get().await()
            Log.d(TAG,"continue")
            if (dataSnapshot.exists()) {
                Log.d(TAG,"continue")
                if(dataSnapshot.children.count() == recipess.size){
                    return
                }

                for (children in dataSnapshot.children) {
                    Log.d(TAG,"continue")

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
                    viewedList[recipe.name] = false

                    Log.d(TAG,"Added")
                }
            }

            recipess = recipes
            Log.d(TAG,"DONE")
/*
            Log.d("ZAAuth", recipes.toString())
*/
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            e.printStackTrace()
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
            favoritesList.add(favorite.id,favoriteMap)
        }
        userMap["favorites"] = favoritesList
        // Push the user data to the "users" node in Firebase
        try {
            firebaseData.child("users").child(user.id.toString()).updateChildren(userMap)
        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
        }
    }


    suspend fun getFavorites(){
        if (favorites.value.isEmpty()) {
            Log.d("ZAAuth","STARTED")
            try {
                val dataSnapshot = firebaseData.child("users").child(currentUser()?.email!!.hashCode().toString()).get().await()
                Log.d(TAG,currentUser()?.email!!.hashCode().toString())
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.value as Map<String, Any>
                    val favors = user["favorites"] as List<Map<String, Long>>
                    val favoritesId = mutableListOf<String>()
                    for (favorite in favors) {
                        Log.d("ZAAuth",favorite.toString())
                        favorite["recipe_id"]?.let { favoritesId.add(it.toString()) }
                        Log.d("ZAAuth",favoritesId.toString())
                    }


                    // Now fetch recipes based on the IDs
                    val recipes = recipess.toList().filter{favoritesId.contains(it.id.toString())}
                    Log.d("ZAAuth", recipes.toString())
                    favorites.value = recipes as MutableList<Recipe>
                    favoriteMeals.value = sortInMeals(favorites.value)

                    Log.d("ZAAuth", favorites.toString())
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                e.printStackTrace()
            }
        }
    }
    fun sortInMeals(_data: MutableList<Recipe>):  MutableList<Meal>{
        Log.d("Favorite",_data.toString())
        // Group recipes by meal name
        val groupedByMeal = _data.groupBy { it.meal }

        // Map the grouped entries to create a list of Meal objects
        val meals = groupedByMeal.map { (mealName, recipes) ->
            Meal(mealName, recipes.toMutableList())
        }
        return meals as MutableList<Meal>
    }

    fun isCurrentRecipeFavorite(recipe: Recipe):Boolean{
        Log.d(TAG,favorites.toString())
        return recipe in favorites.value
    }

    fun addCurrentRecipeToFavorite(recipe: Recipe) {

        // Check if the recipe is already in favorites
        if (!favorites.value.contains(recipe)) {
            // Add the recipe to the local favorites list
            favorites.value.add(recipe)
            Log.d("ZAAuth", favorites.toString())

            favoriteMeals.value = sortInMeals(favorites.value)
            Log.d(TAG,(recipe in favorites.value).toString())

            // Update the user's favorites in the database
        }
    }

    fun removeCurrentRecipeFromFavorite(recipe: Recipe) {
        // Check if the recipe is in favorites
        if (favorites.value.contains(recipe)) {
            // Remove the recipe from the local favorites list
            favorites.value.remove(recipe)
            favoriteMeals.value = sortInMeals(favorites.value)

            // Update the user's favorites in the database
        }
    }

    fun addCurrentRecipeToSeen(recipe: Recipe) {
        if(viewedList.contains(recipe.name)){
            if(!viewedList[recipe.name]!!)
                viewedList[recipe.name] = true
        }
    }

    fun updateFavoritesInDatabase() {
        try {
            // Get the current user's email hash as a unique identifier
            val userHash = currentUser()?.email!!.hashCode().toString()

            // Get the reference to the user's data in the database
            val userRef = firebaseData.child("users").child(userHash)

            // Get the list of recipe IDs from the current favorites
            val favoriteIds = favorites.value.map { it.id.toString() }

            // Create a list of maps representing the favorites in the correct format
            val favoritesList = favoriteIds.map { recipeId ->
                mapOf("id" to recipeId.hashCode().toLong())
                mapOf("recipe_id" to recipeId.toLong())
            }

            // Update the 'favorites' node in the user's data with the new favorites list
            userRef.updateChildren(mapOf("favorites" to favoritesList))

            // Log success or handle other actions if needed
            Log.d("ZAAuth", "Favorites updated and saved to the database.")

        } catch (e: Exception) {
            // Log the error or handle it appropriately
            Log.e(TAG, "Error updating favorites: ${e.message}", e)
        }
    }


    fun setCurrentRecipe(recipe: Recipe){
        currentRecipe.value = recipe

    }

    fun updateSeenInDatabase() {
        for (entry in viewedList.entries) {
            val recipeName = entry.key
            val viewed = entry.value

            if (!viewed) {
                continue
            }

            try {
                // Use the Firebase SDK to increment the "views" field
                val updates: MutableMap<String, Any> = hashMapOf(
                    "recipes/$recipeName/views" to ServerValue.increment(1)
                )

                firebaseData.updateChildren(updates)
                viewedList[recipeName] = false

                Log.d("Zaauth", "Incremented views for recipe: $recipeName")
            } catch (e: Exception) {
                Log.e("Zaauth", "Error updating views for recipe: $recipeName, ${e.message}", e)
            }
        }
    }






}
