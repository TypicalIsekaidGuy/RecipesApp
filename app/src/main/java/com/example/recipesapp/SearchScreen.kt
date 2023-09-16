package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.MaterialText
import com.example.recipesapp.ui.theme.TextBlack

@Composable
fun SearchScreen(controller: NavHostController, viewModel: SearchViewModel) {

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
    val sortList = listOf(
        SortElement("All", Sort.ALL, { viewModel.sortByName() }) { viewModel.sortByDefault() },
        SortElement("Main Course", Sort.MAIN_COURSE, { viewModel.sortByPrice() }) { viewModel.sortByDefault() },
        SortElement("Breakfast", Sort.BREAKFAST, {  viewModel.sortByTrend() }) {viewModel.sortByDefault()  },
        SortElement("Salad %", Sort.SALAD, { viewModel.sortByPercentage() }) { viewModel.sortByDefault() }
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        SearchTopBar()
        SearchSortBar(modifier = Modifier, sortList = sortList )
    }
}
@Composable
fun SearchTopBar(){
    Box(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()){
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)){
                    Image(painter = painterResource(id = R.drawable.test_avatar_pic), contentDescription = null )
                }


                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_32),
                        contentDescription = "Icon 2",
                        modifier = Modifier.clickable {  },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            MaterialText(text = "Get cooking today!",modifier =  Modifier, textStyle = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center )

        }
    }
}
@Composable
fun SearchSortBar(modifier: Modifier, sortList: List<SortElement>){
    val pickedId = remember{
        mutableStateOf(-1)
    }
    Column(modifier = modifier.fillMaxWidth()) {

        LazyRow(modifier = Modifier.padding(top = 16.dp)){
            items(sortList.size){item->
                SortItem(sortItem = sortList[item], pickedId = pickedId, colorScheme = MaterialTheme.colorScheme)
            }
        }
    }
}
@Composable
fun SortItem(
    sortItem: SortElement,
    pickedId: MutableState<Int>,
    colorScheme: ColorScheme // Pass the color scheme as a parameter
) {
    Box(
        modifier = Modifier.padding(start = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.onPrimary
                    else colorScheme.onSecondary
                )
                .clickable {
                    if (pickedId.value == sortItem.sortBy.ordinal) {
                        pickedId.value = -1
                        sortItem.onClickDefault()
                    } else {
                        pickedId.value = sortItem.sortBy.ordinal
                        sortItem.onClick()
                    }
                }
        ) {
            val circleRadius = size.minDimension / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            // Draw the circle with a 1.dp line on the outside
            drawCircle(
                color = TextBlack,
                center = Offset(centerX, centerY),
                radius = circleRadius - 1.dp.toPx()
            )

            // Draw the text inside the circle
            val textPaint = android.graphics.Paint().apply {
                textSize = 12.sp.toPx() / density
                color = if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.onBackground.toArgb()
                else colorScheme.onPrimary.toArgb()
            }
            val textWidth = textPaint.measureText(sortItem.name)
            val textHeight = textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    sortItem.name,
                    centerX - (textWidth / 2f),
                    centerY + (textHeight / 2f) - textPaint.fontMetrics.descent,
                    textPaint
                )
            }
        }
    }
}


@Composable
fun PickTypeBar(){

}
@Composable
fun RecipePreviewList(){

}
@Composable
fun RecipePreviewItem(){

}