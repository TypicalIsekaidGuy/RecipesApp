package com.example.recipesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun UserScreen(navController: NavController){
    val isLoginScreen = remember { mutableStateOf(true) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)) {
            TopUserBar()
            EditTextLine(name = "Login", editTextName = "Enter login")
            EditTextLine(name = "Password", editTextName = "Enter password")
            EditTextLine(name = "Email", editTextName = "Enter email")
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp),horizontalAlignment = Alignment.CenterHorizontally,){
            ConfirmButton()
            SearchOrLoginLine(isLoginScreen)
        }
    }
}
@Composable
fun TopUserBar(){
    Text(text = "Welcome to RePiPi",
        fontStyle = MaterialTheme.typography.displayLarge.fontStyle,
        fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
        fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
        fontSize = MaterialTheme.typography.displayLarge.fontSize,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextLine(name: String, editTextName: String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
        var text by remember { mutableStateOf("") }
        Text(text = name,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,)
        TextField(value = text, onValueChange = {text = it}, placeholder = { Text(text = editTextName) }, colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colorScheme.tertiary, containerColor = MaterialTheme.colorScheme.secondary) )
    }
}
@Composable
fun ConfirmButton(){
    Box(modifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.secondary)
        .height(48.dp)
        .width(256.dp)
        .clickable { }){
        Text("Confirm",
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
    }
}
@Composable
fun SearchOrLoginLine(isLoginScreen: MutableState<Boolean>){
    Row(/*horizontalArrangement = Arrangement.spacedBy(16.dp)*/){

        Box(modifier = Modifier
            .clip(CircleShape)
            .padding(top = 12.dp)
            .clickable { isLoginScreen.value = true }){
            Text("Log in",
                modifier = Modifier.fillMaxWidth(),
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                textAlign = TextAlign.Center)
        }
        Box(modifier = Modifier
            .clip(CircleShape)
            .padding(top = 12.dp)
            .clickable { isLoginScreen.value = false }){
            Text("Create account",
                modifier = Modifier.fillMaxWidth(),
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                textAlign = TextAlign.Center)
        }
    }
}