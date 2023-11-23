package com.example.recipesapp

import android.annotation.SuppressLint

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipesapp.ui.MaterialText

@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchScreen(controller: NavHostController, viewModel: SearchViewModel) {





    val recipes = viewModel.data
    val sortItems = viewModel.sort_list

    val selectorState = mutableStateOf(false)
    var selectedItemIndex = -1


    val searchText = viewModel.searchText
    val isSearching by viewModel.isSearching.collectAsState()

    val list = NavItems(onClick1 = {}, onClick2 = {}).nav_items

/*    val list_ingredients1 = listOf<Ingredient>(
        Ingredient("Lettuce".hashCode(), "Lettuce", 2.0f, true),
        Ingredient("Cucumbers".hashCode(), "Cucumbers", 1.0f, true),
        Ingredient("Tomatoes".hashCode(), "Tomatoes", 2.0f, true),
        Ingredient("Bell Peppers".hashCode(), "Bell Peppers", 1.0f, true),
        Ingredient("Red Onions".hashCode(), "Red Onions", 0.5f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.25f, true),
        Ingredient("Vinegar".hashCode(), "Vinegar", 0.1f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Pepper".hashCode(), "Pepper", 0.02f, true)
    )
    val list_ingredients2 = listOf(
        Ingredient("Spaghetti".hashCode(), "Spaghetti", 200.0f, true),
        Ingredient("Eggs".hashCode(), "Eggs", 2.0f, true),
        Ingredient("Bacon".hashCode(), "Bacon", 150.0f, true),
        Ingredient("Parmesan Cheese".hashCode(), "Parmesan Cheese", 0.2f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.05f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.1f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
    )
    val list_ingredients3 = listOf(
        Ingredient("Chicken Breast".hashCode(), "Chicken Breast", 2.0f, true),
        Ingredient("Lettuce".hashCode(), "Lettuce", 2.0f, true),
        Ingredient("Tomatoes".hashCode(), "Tomatoes", 2.0f, true),
        Ingredient("Cucumbers".hashCode(), "Cucumbers", 1.0f, true),
        Ingredient("Red Onions".hashCode(), "Red Onions", 0.5f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.25f, true),
        Ingredient("Lemon Juice".hashCode(), "Lemon Juice", 0.1f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.02f, true)
    )
    val list_ingredients4 = listOf(
        Ingredient("Arborio Rice".hashCode(), "Arborio Rice", 200.0f, true),
        Ingredient("Mushrooms".hashCode(), "Mushrooms", 150.0f, true),
        Ingredient("Onions".hashCode(), "Onions", 0.5f, true),
        Ingredient("White Wine".hashCode(), "White Wine", 0.2f, true),
        Ingredient("Parmesan Cheese".hashCode(), "Parmesan Cheese", 0.2f, true),
        Ingredient("Butter".hashCode(), "Butter", 0.1f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.1f, true),
        Ingredient("Vegetable Broth".hashCode(), "Vegetable Broth", 0.2f, true),
        Ingredient("Garlic".hashCode(), "Garlic", 0.05f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.02f, true)
    )
    val list_ingredients5 = listOf(
        Ingredient("Salmon Fillet".hashCode(), "Salmon Fillet", 2.0f, true),
        Ingredient("Lemon".hashCode(), "Lemon", 1.0f, true),
        Ingredient("Dill".hashCode(), "Dill", 0.1f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.25f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.02f, true)
    )
    val list_ingredients6 = listOf(
        Ingredient("Broccoli".hashCode(), "Broccoli", 1.0f, true),
        Ingredient("Bell Peppers".hashCode(), "Bell Peppers", 1.0f, true),
        Ingredient("Carrots".hashCode(), "Carrots", 1.0f, true),
        Ingredient("Zucchini".hashCode(), "Zucchini", 1.0f, true),
        Ingredient("Soy Sauce".hashCode(), "Soy Sauce", 0.1f, true),
        Ingredient("Sesame Oil".hashCode(), "Sesame Oil", 0.1f, true),
        Ingredient("Garlic".hashCode(), "Garlic", 0.05f, true),
        Ingredient("Ginger".hashCode(), "Ginger", 0.05f, true),
        Ingredient("Rice".hashCode(), "Rice", 200.0f, true),
    )
    val list_ingredients7 = listOf(
        Ingredient("Pizza Dough".hashCode(), "Pizza Dough", 1.0f, true),
        Ingredient("Tomato Sauce".hashCode(), "Tomato Sauce", 0.2f, true),
        Ingredient("Mozzarella Cheese".hashCode(), "Mozzarella Cheese", 0.2f, true),
        Ingredient("Pepperoni".hashCode(), "Pepperoni", 0.1f, true),
        Ingredient("Bell Peppers".hashCode(), "Bell Peppers", 0.2f, true),
        Ingredient("Onions".hashCode(), "Onions", 0.1f, true),
        Ingredient("Mushrooms".hashCode(), "Mushrooms", 0.2f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.1f, true),
        Ingredient("Basil".hashCode(), "Basil", 0.02f, true),
    )
    val list_ingredients8 = listOf(
        Ingredient("Ground Beef".hashCode(), "Ground Beef", 0.3f, true),
        Ingredient("Taco Shells".hashCode(), "Taco Shells", 4.0f, true),
        Ingredient("Lettuce".hashCode(), "Lettuce", 1.0f, true),
        Ingredient("Tomatoes".hashCode(), "Tomatoes", 1.0f, true),
        Ingredient("Onions".hashCode(), "Onions", 0.5f, true),
        Ingredient("Cheddar Cheese".hashCode(), "Cheddar Cheese", 0.2f, true),
        Ingredient("Sour Cream".hashCode(), "Sour Cream", 0.1f, true),
        Ingredient("Taco Seasoning".hashCode(), "Taco Seasoning", 0.05f, true),
    )
    val list_ingredients9 = listOf(
        Ingredient("Pumpkin".hashCode(), "Pumpkin", 1.0f, true),
        Ingredient("Onions".hashCode(), "Onions", 0.5f, true),
        Ingredient("Garlic".hashCode(), "Garlic", 0.05f, true),
        Ingredient("Vegetable Broth".hashCode(), "Vegetable Broth", 0.3f, true),
        Ingredient("Heavy Cream".hashCode(), "Heavy Cream", 0.2f, true),
        Ingredient("Nutmeg".hashCode(), "Nutmeg", 0.02f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.02f, true)
    )
    val list_ingredients10 = listOf(
        Ingredient("Whole Chicken".hashCode(), "Whole Chicken", 1.0f, true),
        Ingredient("Lemon".hashCode(), "Lemon", 2.0f, true),
        Ingredient("Garlic".hashCode(), "Garlic", 0.1f, true),
        Ingredient("Rosemary".hashCode(), "Rosemary", 0.05f, true),
        Ingredient("Thyme".hashCode(), "Thyme", 0.05f, true),
        Ingredient("Olive Oil".hashCode(), "Olive Oil", 0.1f, true),
        Ingredient("Salt".hashCode(), "Salt", 0.05f, true),
        Ingredient("Black Pepper".hashCode(), "Black Pepper", 0.02f, true)
    )
    val recipe1 = Recipe(
        "Vegan Mix Vegetable Caesar".hashCode(),
        "Vegan Mix Vegetable Caesar",
        imageBitmap,
        20, // Prepare time set to 20 minutes
        1,
        "Instructions for Vegan Mix Vegetable Caesar",
        list_ingredients1,
        "1. Wash and chop lettuce, cucumbers, tomatoes, bell peppers, and red onions.\n" +
                "2. In a bowl, mix olive oil, vinegar, salt, and pepper to make the dressing.\n" +
                "3. Toss the chopped vegetables with the dressing and serve."
    )

    val recipe2 = Recipe(
        "Spaghetti Carbonara".hashCode(),
        "Spaghetti Carbonara",
        imageBitmap,
        30, // Prepare time set to 30 minutes
        1,
        "Instructions for Spaghetti Carbonara",
        list_ingredients2,
        "1. Cook spaghetti according to package instructions.\n" +
                "2. In a separate pan, fry bacon until crispy.\n" +
                "3. Whisk eggs and mix in grated Parmesan cheese.\n" +
                "4. Drain spaghetti and toss with egg and cheese mixture. Add bacon.\n" +
                "5. Season with salt and pepper. Serve."
    )

    val recipe3 = Recipe(
        "Grilled Chicken Salad".hashCode(),
        "Grilled Chicken Salad",
        imageBitmap,
        15,
        1,
        "Instructions for Grilled Chicken Salad",
        list_ingredients3,
        "1. Grill chicken until cooked through and slightly charred.\n" +
                "2. Chop lettuce, tomatoes, cucumbers, and red onions.\n" +
                "3. Mix olive oil and lemon juice to make the dressing.\n" +
                "4. Toss grilled chicken and vegetables with the dressing and serve."
    )

    val recipe4 = Recipe(
        "Mushroom Risotto".hashCode(),
        "Mushroom Risotto",
        imageBitmap,
        35,
        1,
        "Instructions for Mushroom Risotto",
        list_ingredients4,
        "1. Sauté onions and garlic in olive oil until soft.\n" +
                "2. Add Arborio rice and cook for a few minutes.\n" +
                "3. Gradually add vegetable broth and white wine while stirring.\n" +
                "4. Stir in mushrooms, Parmesan cheese, and butter until creamy. Serve hot."
    )

    val recipe5 = Recipe(
        "Pan-Seared Salmon".hashCode(),
        "Pan-Seared Salmon",
        imageBitmap,
        25,
        1,
        "Instructions for Pan-Seared Salmon",
        list_ingredients5,
        "1. Season salmon fillet with salt, pepper, and lemon juice.\n" +
                "2. Heat olive oil in a pan and sear the salmon until cooked through.\n" +
                "3. Sprinkle with fresh dill. Serve with your favorite side dishes."
    )

    val recipe6 = Recipe(
        "Vegetable Stir-Fry".hashCode(),
        "Vegetable Stir-Fry",
        imageBitmap,
        20,
        1,
        "Instructions for Vegetable Stir-Fry",
        list_ingredients6,
        "1. Heat sesame oil in a wok or pan.\n" +
                "2. Add chopped vegetables and stir-fry until tender-crisp.\n" +
                "3. Stir in soy sauce, garlic, and ginger. Serve with cooked rice."
    )

    val recipe7 = Recipe(
        "Homemade Pizza".hashCode(),
        "Homemade Pizza",
        imageBitmap,
        40,
        1,
        "Instructions for Homemade Pizza",
        list_ingredients7,
        "1. Roll out pizza dough and spread tomato sauce.\n" +
                "2. Sprinkle with mozzarella cheese and add your favorite toppings.\n" +
                "3. Bake in a preheated oven until the crust is golden. Slice and enjoy."
    )

    val recipe8 = Recipe(
        "Beef Tacos".hashCode(),
        "Beef Tacos",
        imageBitmap,
        30,
        1,
        "Instructions for Beef Tacos",
        list_ingredients8,
        "1. Brown ground beef in a skillet and add taco seasoning.\n" +
                "2. Warm taco shells in the oven.\n" +
                "3. Assemble tacos with beef, lettuce, tomatoes, cheese, and sour cream."
    )

    val recipe9 = Recipe(
        "Pumpkin Soup".hashCode(),
        "Pumpkin Soup",
        imageBitmap,
        25,
        1,
        "Instructions for Pumpkin Soup",
        list_ingredients9,
        "1. Roast pumpkin with onions and garlic in the oven.\n" +
                "2. Puree the roasted vegetables with vegetable broth.\n" +
                "3. Add cream, nutmeg, salt, and pepper. Reheat and serve."
    )

    val recipe10 = Recipe(
        "Lemon Herb Roasted Chicken".hashCode(),
        "Lemon Herb Roasted Chicken",
        imageBitmap,
        40,
        1,
        "Instructions for Lemon Herb Roasted Chicken",
        list_ingredients10,
        "1. Rub chicken with olive oil, lemon, and herbs.\n" +
                "2. Roast in the oven until golden and cooked through. Serve with your favorite sides."
    )


    val list = mutableListOf(
        recipe1,
        recipe2,
        recipe3,
        recipe4,
        recipe5,
        recipe6,
        recipe7,
        recipe8,
        recipe9,
        recipe10,
    )
    viewModel.load(list)
    val meals = mutableListOf(
        Meal("Salad",list),
        Meal("Cherry",list),
        Meal("Main Course",list)
    )*/

    ModalNavigationDrawer(                        drawerContent = {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(16.dp))
            list.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    label = {
                        Text(text = item.title)
                    },
                    selected = index == selectedItemIndex,
                    onClick = {
//                                            navController.navigate(item.route)
                        selectedItemIndex = index
                        controller.navigate(Screen.FavoriteScreen.route)
/*                        scope.launch {
                            drawerState.close()
                        }*/
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    badge = {
                        item.badgeCount?.let {
                            Text(text = item.badgeCount.toString())
                        }
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    },) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            SearchTopBar(){controller.navigate(Screen.FavoriteScreen.route)}
            SearchSortBar(modifier = Modifier, sortList = sortItems )
            SearchTextField(viewModel, onTextFieldValueChange = { viewModel.onSearchChange(searchText.value) })
            RecipePreviewList(recipes.value){ recipe ->
                viewModel.setCurrentRecipe(recipe)
                navigateToMainScreen(controller)
            }
        }
    }
}
fun navigateToMainScreen(controller: NavHostController){
    controller.navigate("${Screen.MainScreen.route}")
}
@Composable
fun SearchTopBar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_32),
                        contentDescription = "Icon 2",
                        modifier = Modifier.clickable { onClick() },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test_avatar_pic),
                        contentDescription = null
                    )
                }
            }
            MaterialText(
                text = "Get cooking today!",
                modifier = Modifier,
                textStyle = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
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
                    if (pickedId.value == sortItem.order) colorScheme.onSecondary
                    else colorScheme.tertiary
                )
                .clickable {
                    if (pickedId.value == sortItem.order) {
                        pickedId.value = -1
                    } else {
                        pickedId.value = sortItem.order
                        sortItem.onClick()
                    }
                }
                .padding(8.dp)
        ) {
            MaterialText(text = sortItem.name, textStyle = MaterialTheme.typography.headlineMedium, modifier = Modifier,color = if (pickedId.value == sortItem.order) colorScheme.tertiary
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
fun RecipePreviewList(recipes: List<Recipe>,  onClick: (Recipe) -> Unit) {
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
                        */
        ) { onClick(recipes[page]) }
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
    recipe: Recipe,
    openRecipe: ()-> Unit
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
                        .clickable { openRecipe() }
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
                                text = recipe.prepareTime.minsToString(),
                                fontSize = 16.sp
                            )
                            UnderneathSpecifier(
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.onSecondary,
                                text = recipe.views.viewsToString(),
                                fontSize = 16.sp
                            )
                        }
                    }
                }



}
