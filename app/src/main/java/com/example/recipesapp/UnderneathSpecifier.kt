package com.example.recipesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnderneathSpecifier(
    modifier : Modifier,
    color: Color,
    text: String
){
    Box(modifier = modifier.background(MaterialTheme.colorScheme.secondary).clip(RoundedCornerShape(24.dp)).padding(horizontal = 6.dp)){
        Text(fontSize = 12.sp, color = color, text = text)
    }
}