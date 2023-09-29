package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.MaterialText

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
        SortElement("Salad", Sort.SALAD, { viewModel.sortByPercentage() }) { viewModel.sortByDefault() }
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        SearchTopBar()
        SearchSortBar(modifier = Modifier, sortList = sortList )
        RecipePreviewList(Meal("Salad",list))
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
            MaterialText(
                text = "Get cooking today!",
                modifier =  Modifier, textStyle = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center )

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
    colorScheme: ColorScheme
) {
    Box(
        modifier = Modifier.padding(start = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        val isRectangle = sortItem.name.length * 48 > 20
        val backgroundColor =
            if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.onSecondary
            else colorScheme.tertiary

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(
                    if (isRectangle) RoundedCornerShape(12.dp)
                    else CircleShape
                )
                .background(backgroundColor)
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
            Text(
                text = sortItem.name,
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                color = if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.tertiary
                else colorScheme.onSecondary
            )
        }
    }
}






@Composable
fun PickTypeBar(){

}
@Composable
fun RecipePreviewList(meal: Meal) {
    val scrollState = rememberLazyListState()
    val itemCount = meal.recipies.size

    LazyRow(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
    ) {
        itemsIndexed(meal.recipies) { index, recipe ->
            val itemSize by animateDpAsState(
                targetValue = if (index == scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.index) 120.dp else 80.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            RecipePreviewItem(
                recipe = recipe,
                true
/*                size = itemSize,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            */)
        }
    }
}

/*@Composable
fun RecipePreviewList(meal: Meal) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(bottom = 16.dp, top = 16.dp)
            .fillMaxWidth()
    ) {
        // Content to be revealed
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                items(meal.recipies.size){index->
                    RecipePreviewItem(recipe = meal.recipies[index])
                }
            }

    }
}*/
@Composable
fun RecipePreviewItem(
    recipe: RecipePreview,
    isFocused: Boolean
) {
    val targetSize = if (isFocused) 1f else 0.7f // Adjust the scaling factor as needed
    val alpha = if (isFocused) 1f else 0.7f // Adjust the alpha for fading effect

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
            .graphicsLayer(
                scaleX = animateFloatAsState(targetValue = targetSize).value,
                scaleY = animateFloatAsState(targetValue = targetSize).value,
                alpha = animateFloatAsState(targetValue = alpha).value
            )
    ) {
        Image(
            bitmap = recipe.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            MaterialText(
                text = recipe.name,
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
                    text = "${recipe.prepareTime} mins",
                    fontSize = 16.sp
                )
                UnderneathSpecifier(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "${recipe.views}k views",
                    fontSize = 16.sp
                )
            }
        }
    }
}
