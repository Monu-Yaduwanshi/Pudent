//package com.example.pudent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.pudent.ui.theme.PudentTheme
//import com.google.firebase.FirebaseApp
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        FirebaseApp.initializeApp(this)
//
//        setContent {
//            PudentTheme {
//                val authViewModel: AuthViewModel = viewModel()
//                AppNavigation(authViewModel)
//            }
//        }
//    }
//}



//package com.example.pudent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.pudent.ui.theme.PudentTheme
//import com.google.firebase.FirebaseApp
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(this)
//        setContent {
//            PudentTheme {
//                val authViewModel: AuthViewModel = viewModel()
//                AppNavigation(authViewModel)
//            }
//        }
//    }
//}


package com.example.pudent

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pudent.ui.theme.PudentTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Instantiate BleManager
        val bleManager = BleManager(context = this)

        setContent {
            PudentTheme {
                val authViewModel: AuthViewModel = viewModel()
                AppNavigation(authViewModel = authViewModel, bleManager = bleManager)
            }
        }
    }
}
