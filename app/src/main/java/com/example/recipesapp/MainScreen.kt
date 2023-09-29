package com.example.recipesapp

import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.example.recipesapp.ui.MaterialText

@Composable
fun MainScreen(controller: NavHostController, viewModel: MainViewModel) {

    val imageResource = R.drawable.test_image // Replace with your image resource name
    val context = LocalContext.current
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResource)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

    val recipe = RecipePreview("Vegan Mix Vegetable Ceaser Salad",imageBitmap,20,140, true )
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item{
            MainTopBar{controller.popBackStack()}
        }
        item{
            MaterialText(text = recipe.name, textStyle = MaterialTheme.typography.displayLarge, modifier = Modifier, lineHeight = 32.sp)
        }
        item{
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
        }
        item{
            MaterialText(text = "ROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooooooooooooooooooooooooooooooooooooooooooooooooo", textStyle = MaterialTheme.typography.headlineMedium, modifier = Modifier, lineHeight = 24.sp)
        }
        item{
            ClarificationBar()
        }
        item{
            DescriptionItem("Ingridients", "Lorem Ispum", MaterialTheme.colorScheme.surface)
        }
        item{
            DescriptionItem("Description", "Lorem Ispum", MaterialTheme.colorScheme.surfaceVariant)
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
fun ClarificationBar(){
    Row(){

    }
}
@Composable
fun ServingsClarification(){
    //take it from restaurant app
}
@Composable
fun DetailsClarification(){

}
@Composable
fun DescriptionItem(
    name: String,
    text: String,
    backgroundColor: Color
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        // Add any content you want above the image here.
    }
    Box(
        modifier = Modifier
            .size(width = 270.dp, height = 480.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            MaterialText(
                text = name,
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineMedium,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                UnderneathSpecifier(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "${text} mins",
                    fontSize = 16.sp
                )
                UnderneathSpecifier(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "${text}k views",
                    fontSize = 16.sp
                )
            }
        }
    }
}
