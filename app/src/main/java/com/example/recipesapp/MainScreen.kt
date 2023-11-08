package com.example.recipesapp

import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.recipesapp.ui.MaterialText

@Composable
fun MainScreen(controller: NavHostController, viewModel: MainViewModel, recipe: Recipe) {

    val imageResource = R.drawable.test_image // Replace with your image resource name
    val context = LocalContext.current
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResource)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val recipeArg = navBackStackEntry?.arguments?.getString("recipeId")?.let { recipeId ->
        Log.d("TAGGGG",recipeId.toString())
    }
    val list_ingridients = listOf<Ingredient>(
        Ingredient("pepper".hashCode(),"pepper",0.5f,true),
        Ingredient("peppe".hashCode(),"peppe",0.5f,true),
        Ingredient("pepp".hashCode(),"pepp",0.5f,true)
    )
/*
    val recipe =         Recipe("Vegan Mix Vegetable Ceaser".hashCode(),"Vegan Mix Vegetable Ceaser",imageBitmap,20,140, "salad",list_ingridients, "This is easeily done")
*/
    val servings by viewModel.servings
    LazyColumn(modifier = Modifier
        .fillMaxWidth()) {
        item{
            Column(modifier = Modifier
                .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {

                MainTopBar{controller.popBackStack()}
                MaterialText(text = recipe.name, textStyle = MaterialTheme.typography.displayLarge, modifier = Modifier
                    , lineHeight = 32.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 600.dp, height = 200.dp) // Set the image height to 600dp
                        .clip(shape = RoundedCornerShape(16.dp)) // Clip round edges with a 16dp radius
                        .background(Color.Gray) // Set a background color (you can change this)
                ) {
                    Image(
                        painterResource(id = imageResource),
                        contentDescription = null, // Provide a description if needed
                        contentScale = ContentScale.Crop, // Crop the image to fit the box
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center,
                    )
                }
                MaterialText(text = "ROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooooooooooooooooooooooooooooooooooooooooooooooooo", textStyle = MaterialTheme.typography.headlineMedium, modifier = Modifier
                    , lineHeight = 24.sp)
                ClarificationBar(servings, viewModel)
            }
        }
        item{
            Column(){

                DescriptionItem("Ingridients", "Lorem Ispum", MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(-8.dp)) // Adjust the negative margin as needed
                DescriptionItem("Description", "Lorem Ispumssssssssssssssssssssssssssssssssssssssssssssss", MaterialTheme.colorScheme.surfaceVariant, topBackgroundColor = MaterialTheme.colorScheme.secondary)

            }
        }
    }
}
@Composable
fun MainTopBar(goBackToScreen: ()-> Unit){
    Box(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()){
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_exit_24),
                        contentDescription = "Icon 2",
                        modifier = Modifier.clickable { goBackToScreen() },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                MaterialText(
                    text = "Recipe",
                    modifier =  Modifier, textStyle = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center )



                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_lock_24),
                        contentDescription = "Icon 2",
                        modifier = Modifier.clickable {  },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
@Composable
fun ClarificationBar(servings: Int, viewModel: MainViewModel){

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
        ServingsClarification(servings, viewModel)
        DetailsClarification("Preparation", viewModel.strTotalPrepTime)
        DetailsClarification("Cook", viewModel.strTotalCookTime)
    }
}
@Composable
fun ServingsClarification(servings: Int, viewModel: MainViewModel){
    Column(modifier = Modifier) {
        MaterialText(text = "Servings",
            modifier = Modifier,
            textStyle = MaterialTheme.typography.headlineMedium)
        PlusMinusButton(servings, { viewModel.addServings() }) { viewModel.decreaseServings() }
    }
}
@Composable
fun PlusMinusButton(servings: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onIncrease() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = servings.toString(),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    servings < 10 -> 20.sp
                    servings < 100 -> 18.sp
                    else -> 16.sp
                }
            )

            IconButton(
                onClick = { onDecrease() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun DetailsClarification(name: String, time: String){
    Column(modifier = Modifier) {
        MaterialText(text = name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.headlineMedium)
        MaterialText(text = time,
            modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)

    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DescriptionItem(
    name: String,
    text: String,
    backgroundColor: Color,
    topBackgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(topBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp, // Round only the top corners
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp // Leave the bottom corners straight
                    )
                )
                .background(backgroundColor)
                .clickable { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MaterialText(
                    text = name,
                    textStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Box(modifier = Modifier
                .background(backgroundColor)){

                MaterialText(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
