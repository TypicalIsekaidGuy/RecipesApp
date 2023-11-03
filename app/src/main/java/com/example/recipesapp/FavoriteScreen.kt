package com.example.recipesapp

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.MaterialText


@Composable
fun FavoriteScreen(controller: NavHostController, viewModel: FavoriteViewModel){

    val imageResource = R.drawable.test_image // Replace with your image resource name
    val context = LocalContext.current
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResource)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()


    val list_ingridients = listOf<Ingridient>(
        Ingridient("pepper".hashCode(),"pepper",0.5f,true),
        Ingridient("peppe".hashCode(),"peppe",0.5f,true),
        Ingridient("pepp".hashCode(),"pepp",0.5f,true)
    )
    val list = mutableListOf(
        Recipe("Vegan Mix Vegetable Ceaser".hashCode(),"","Vegan Mix Vegetable Ceaser",imageBitmap,20,140, "salad",list_ingridients),
        Recipe("Vegan Mix Vegetable ".hashCode(),"","Vegan Mix Vegetable ",imageBitmap,20,140, "salad",list_ingridients),
        Recipe("Vegan Mix  Ceaser".hashCode(),"","Vegan Mix  Ceaser",imageBitmap,20,140, "salad",list_ingridients),
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
                modifier = Modifier.clickable { goBackToScreen() },
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Box(modifier = Modifier.align(Alignment.Center)){

            MaterialText(text = "Favorite Recipe", modifier = Modifier, textStyle = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center)
        }
            Spacer(modifier = Modifier.fillMaxWidth())

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
                Text(text = meal.name, textAlign = TextAlign.Left, fontSize = 32.sp, color = MaterialTheme.colorScheme.tertiary)
                Text(text = " ${meal.recipies.size}", color = MaterialTheme.colorScheme.tertiary)
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
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                items(meal.recipies.size){index->
                    MealItem(recipe = meal.recipies[index])
                }
            }
        }
}
}
@Composable
fun MealItem(recipe: Recipe){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
        Box( modifier = Modifier.clip(
            RoundedCornerShape(12.dp)
        )){
            Image(bitmap = recipe.image, contentDescription = null, modifier = Modifier.clip(
                RoundedCornerShape(12.dp)
            ))
        }
        Text(recipe.name, textAlign = TextAlign.Center, fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            UnderneathSpecifier(Modifier, MaterialTheme.colorScheme.tertiary, "${ recipe.prepareTime } mins")
            UnderneathSpecifier(Modifier, MaterialTheme.colorScheme.tertiary, "${ recipe.views }k views ")
        }
    }
}