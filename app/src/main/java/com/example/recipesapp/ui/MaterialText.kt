package com.example.recipesapp.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun MaterialText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier,
    textAlign: TextAlign? = null,
    color: Color = MaterialTheme.colorScheme.tertiary,
    lineHeight: TextUnit = 10.sp
){

    Text(text = text,
        modifier = modifier,// find a way to make this not important
        fontStyle = textStyle.fontStyle,
        fontWeight = textStyle.fontWeight,
        fontFamily = textStyle.fontFamily,
        fontSize = textStyle.fontSize,
        textAlign = textAlign,
        color = color,
        lineHeight = lineHeight
    )
}