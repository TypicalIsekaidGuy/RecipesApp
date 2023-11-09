package com.example.recipesapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationScreenItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val onClick:() ->Unit
)
class NavItems(badgeCount1: Int? = null, badgeCount2: Int? = null,onClick1:()->Unit,onClick2:()->Unit) {
    val nav_items = listOf(NavigationScreenItem("Search", Icons.Filled.Info, Icons.Filled.Info,badgeCount1
    ) { onClick1() },NavigationScreenItem("Favorites", Icons.Filled.Close, Icons.Filled.Info,badgeCount2) { onClick2() })
}