package com.example.recipesapp

import com.google.firebase.database.DatabaseReference

class RecipeRepository {

    // Firebase Database reference
/*    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Function to fetch recipes from Firebase
    suspend fun getRecipes(): List<RecipePreview> {
        return suspendCoroutine { continuation ->
            databaseReference.child("recipes").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipes = mutableListOf<Recipe>()
                    for (recipeSnapshot in snapshot.children) {
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        recipe?.let { recipes.add(it) }
                    }
                    continuation.resume(recipes)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }*/
}
