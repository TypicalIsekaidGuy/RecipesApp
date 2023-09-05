package com.example.recipesapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.theme.RecipesAppTheme
import com.example.recipesapp.ui.theme.firaSansFamily


@Composable
fun FavoriteScreen(controller: NavHostController) {

    val imageResource = R.drawable.test_image // Replace with your image resource name
    val context = LocalContext.current
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResource)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()


    val list = mutableListOf(
        RecipePreview("Vegan Mix Vegetable Ceaser Salad",imageBitmap,20,140, true ),
        RecipePreview("Vegan Mix Vegetable Ceaser Salad",imageBitmap,20,140, true ),
        RecipePreview("Vegan Mix Vegetable Ceaser Salad",imageBitmap,20,140, true )
    )
    val meals = mutableListOf(
        Meal("Salad",list),
        Meal("Cherry",list),
        Meal("Main Course",list)
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp)) {
        FavoriteTopBar{controller.popBackStack()}
        MealSlideList(meals)
    }    
}
@Composable
fun FavoriteTopBar(goBackToScreen: ()-> Unit){

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .align(Alignment.TopStart)
            .padding(8.dp)){
            Icon(
                painter = painterResource(id = R.drawable.baseline_exit_24),
                contentDescription = "Icon 2",
                modifier = Modifier.clickable { goBackToScreen() }
            )
        }
        Box(modifier = Modifier.align(Alignment.Center)){

            Text(text = "Favorite Recipe", fontSize = 24.sp, color = MaterialTheme.colorScheme.tertiary, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier)
    }
}
@Composable
fun MealSlideList(meals: List<Meal>) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                for (i in meals)
                    MealSlideItem(meal = i)
            }


}
@Composable
fun MealSlideItem(meal: Meal) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row() {
                Text(text = meal.name, textAlign = TextAlign.Left, fontSize = 32.sp)
                Text(text = " ${meal.recipies.size}")
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { isExpanded = !isExpanded }
                    .rotate(rotationState) // Apply the rotation animation
            )
        }

        Spacer(
            modifier = Modifier
                .size(width = 512.dp, height = 1.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        )
        // Content to be revealed
        AnimatedVisibility(visible = isExpanded) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)){
                items(meal.recipies.size){index->
                    MealItem(recipe = meal.recipies[index])
                }
            }
        }
}
}
@Composable
fun MealItem(recipe: RecipePreview){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
        Image(bitmap = recipe.image, contentDescription = null, modifier = Modifier.clip(
            RoundedCornerShape(12.dp)
        ))
        Text(recipe.name, textAlign = TextAlign.Center, fontSize = 16.sp)
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            UnderneathSpecifier(Modifier, MaterialTheme.colorScheme.tertiary, "${ recipe.prepareTime } mins")
            UnderneathSpecifier(Modifier, MaterialTheme.colorScheme.tertiary, "${ recipe.views }k views ")
        }
    }
}