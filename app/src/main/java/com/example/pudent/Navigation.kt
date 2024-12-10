//package com.example.pudent
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//
//@RequiresApi(Build.VERSION_CODES.S)
//@Composable
//fun AppNavigation(authViewModel: AuthViewModel,bleManager: BleManager) {
//    val bluetoothAdapter = bleManager.bluetoothAdapter
//    val navController = rememberNavController()
//    val selectedItem = remember { mutableStateOf(0) }
//
//    NavHost(navController, startDestination = "LoginScreen") {
////        composable("LoginScreen") { LoginScreen(navController) }
////        composable("SignUpScreen") { SignUpScreen(navController) }
////        composable("home") { HomeScreen(navController) }
//        composable("LoginScreen") { LoginScreen(navController, authViewModel) }
//        composable("SignUpScreen") { SignUpScreen(navController, authViewModel) }
//        composable("home") { HomeScreen(navController = navController) }
//    }
//}
package com.example.pudent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation(authViewModel: AuthViewModel, bleManager: BleManager) {
    val navController = rememberNavController()
    val selectedItem = remember { mutableStateOf(0) }

    NavHost(navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController, authViewModel) }
        composable("SignUpScreen") { SignUpScreen(navController, authViewModel) }
        composable("home") { HomeScreen(navController = navController, bleManager = bleManager) }
    }
}
