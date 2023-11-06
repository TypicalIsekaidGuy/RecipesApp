package com.example.recipesapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.MaterialText

@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchScreen(controller: NavHostController, viewModel: SearchViewModel) {

    val imageResource = R.drawable.test_image // Replace with your image resource name
    val context = LocalContext.current
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResource)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()
    val recipes = viewModel._data.collectAsState()
    for (i in recipes.value) {
        i.image = imageBitmap
    }

    val selectorState = mutableStateOf(false)


    val searchText = viewModel.searchText
    val isSearching by viewModel.isSearching.collectAsState()

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
    viewModel.load(list)
    val meals = mutableListOf(
        Meal("Salad",list),
        Meal("Cherry",list),
        Meal("Main Course",list)
    )
    val sortList = listOf(
        SortElement("All", Sort.ALL, {  }),
        SortElement("Main Course", Sort.MAIN_COURSE, { }),
        SortElement("Breakfast", Sort.BREAKFAST, {   }) ,
        SortElement("Salad", Sort.SALAD, {  })
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        SearchTopBar()
        SearchSortBar(modifier = Modifier, sortList = sortList )
        SearchTextField(viewModel, onTextFieldValueChange = { viewModel.onSearchChange(searchText.value) })
        RecipePreviewList(recipes.value)
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

        LazyRow(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)){
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
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.onSecondary
                    else colorScheme.tertiary
                )
                .clickable {
                    if (pickedId.value == sortItem.sortBy.ordinal) {
                        pickedId.value = -1
                    } else {
                        pickedId.value = sortItem.sortBy.ordinal
                        sortItem.onClick()
                    }
                }
                .padding(8.dp)
        ) {
            MaterialText(text = sortItem.name, textStyle = MaterialTheme.typography.headlineMedium, modifier = Modifier,color = if (pickedId.value == sortItem.sortBy.ordinal) colorScheme.tertiary
            else colorScheme.onSecondary)
        }

}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(viewModel: SearchViewModel, onTextFieldValueChange: (String) ->Unit){
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
/*        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.onSearchChange(text)
                onTextFieldValueChange(text)
                *//*
                                                viewModel.onSearchTextChange(newSearchText)
                *//*
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .fillMaxWidth(),

            textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary),
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )*/
        Column {

            val placeholder = "Enter recipe name"
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                    viewModel.onSearchChange(text)
                    onTextFieldValueChange(text)
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier // margin left and right
                            .fillMaxWidth()
                            .padding(vertical = 12.dp), // inner padding
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 18.sp,
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }

                }
            )
            Box(
                modifier = Modifier // margin left and right
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 8.dp))

        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipePreviewList(recipes: List<Recipe>) {
    val scrollState = rememberLazyListState()
    val itemCount = recipes.size

    val pagerState = rememberPagerState()
    HorizontalPager(pageCount = itemCount,//make it animated like in tutorial
        state = pagerState, modifier = Modifier.padding(top = 8.dp)) {
            page ->

        RecipePreviewItem(
            recipe = recipes[page]
            /*                size = itemSize,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        */)
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
fun RecipesSelector(selectorState: MutableState<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ClickableSelector(
            isSelected = selectorState,
            onClick = {
                // Trigger the function for the Main Course selector
            },
            text = "Soup",
        )
        ClickableSelector(
            isSelected = selectorState,
            onClick = {
                // Trigger the function for the Main Course selector
            },
            text = "Ssssssoup",
        )
        ClickableSelector(
            isSelected = selectorState,
            onClick = {
                // Trigger the function for the Main Course selector
            },
            text = "p",
        )
        ClickableSelector(
            isSelected = selectorState,
            onClick = {
                // Trigger the function for the Main Course selector
            },
            text = "Soup",
        )


    }
}
@Composable
fun ClickableSelector(
    isSelected: MutableState<Boolean>,
    onClick: () -> Unit,
    text: String
) {
    Box(
        modifier = Modifier
            .clickable {
                isSelected.value = true
                onClick()
            }
            .background(if (isSelected.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
    ){
        Text(text = text)
    }
}@Composable
fun RecipePreviewItem(
    recipe: Recipe
) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Add any content you want above the image here.
                }
                Box(
                    modifier = Modifier
                        .size(width = 360.dp, height = 640.dp)
                        .clip(RoundedCornerShape(12.dp))
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
