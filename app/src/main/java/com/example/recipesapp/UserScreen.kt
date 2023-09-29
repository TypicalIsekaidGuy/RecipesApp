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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipesapp.ui.MaterialText

@Composable
fun UserScreen(navController: NavController, userViewModel: UserViewModel){
    val isLoginScreen = remember { mutableStateOf(true) }
    val login = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val name = remember{ mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)) {
            TopUserBar()
            EditTextLine(modifier = Modifier, text = login.value, onTextChanged = {login.value = it}, hint = "Login")
            EditTextLine(modifier = Modifier, text = password.value, onTextChanged = {password.value = it}, hint = "Password")
            EditTextLine(modifier = Modifier, text = name.value, onTextChanged = {name.value = it}, hint = "Name")

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
@Composable
fun ConfirmButton(){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.secondary)
        .height(48.dp)
        .width(256.dp)
        .clickable { }){

        MaterialText("Confirm", MaterialTheme.typography.headlineMedium, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center), textAlign = TextAlign.Center)
    }
}
@Composable
fun SearchOrLoginLine(isLoginScreen: MutableState<Boolean>){
    Row(
        Modifier
            .padding(top = 12.dp)
            .width(256.dp), horizontalArrangement = Arrangement.SpaceAround){

        Box(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { isLoginScreen.value = true }){

            MaterialText("Log in", MaterialTheme.typography.headlineSmall, modifier = Modifier, textAlign = TextAlign.Center)
        }
        Box(modifier = Modifier
            .padding(end = 4.dp, start = 4.dp)
            .clip(CircleShape)
            .clickable { isLoginScreen.value = false }){

            MaterialText("Create account", MaterialTheme.typography.headlineSmall, modifier = Modifier, textAlign = TextAlign.Center)
        }
    }
}@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTextLine(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    hint: String,
    // icon: @DrawableRes Int
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current.density

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.baseline_lock_24),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier.weight(1f) // To make the Column take available horizontal space
        ) {
            Text(
                text = hint,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            BasicTextField(
                value = text,
                onValueChange = {
                    onTextChanged(it)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    .onFocusChanged { focusState ->
                        isTextFieldFocused = focusState.isFocused
                    }
            )
        }
    }
}
