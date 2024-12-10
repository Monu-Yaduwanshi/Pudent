package com.example.pudent

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Get the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFffff))
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    }
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0xFF825534))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.stethoscope)
                    .build(),
                contentDescription = "Company Logo",
                imageLoader = gifEnabledLoader,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sign-In",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val customColor = Color(0xFF455A64)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email", color = customColor) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.login)
                        .build(),
                    contentDescription = "Email Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
                focusedTextColor = customColor,
                cursorColor = customColor
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password", color = customColor) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.twostepverification)
                        .build(),
                    contentDescription = "Password Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                Text(
                    if (showPassword) "Hide" else "Show",
                    Modifier.clickable { showPassword = !showPassword },
                    color = customColor
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
                focusedTextColor = customColor,
                cursorColor = customColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { authViewModel.login(email, password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = customColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        ) {
            Text(text = "Sign-In")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Don't have an account? Sign Up",
            color = customColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("SignUpScreen")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo("LoginScreen") { inclusive = true }
                    }
                }
            }
            is AuthState.Error -> {
                Text(
                    (authState as AuthState.Error).error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            else -> {}
        }
    }
}