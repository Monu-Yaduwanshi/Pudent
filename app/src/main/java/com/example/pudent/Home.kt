//// CODE 1
////package com.example.pudent
////import androidx.compose.foundation.layout.padding
////import androidx.compose.material3.*
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.navigation.NavController
////
////val AboutUsColorScheme = lightColorScheme(
////    primary = Color(0xFF4CAF50), // Vibrant greennn lightt
////    onPrimary = Color.White,
////    background = Color(0xFFCBD6AD), // Light green (#FFF1F7E9) my
////    onBackground = Color.Black,
////    surface = Color.White,
////    onSurface = Color.Black
////)
////
////@OptIn(ExperimentalMaterial3Api::class)
////@Composable
////fun HomeScreen(navController: NavController) {
////    MaterialTheme(colorScheme = AboutUsColorScheme) {
////        Scaffold(
////            topBar = {
////                TopAppBar(
////                    title = { Text("Home") },
////                    colors = TopAppBarDefaults.topAppBarColors(
////                        containerColor = MaterialTheme.colorScheme.primary,
////                        titleContentColor = MaterialTheme.colorScheme.onPrimary
////                    )) },
////            content = { paddingValues ->
////                Surface(
////                    modifier = Modifier.padding(paddingValues),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    Text(
////                        text = "Welcome to Home  Screen m",
////                        style = MaterialTheme.typography.bodyLarge,
////                        color = MaterialTheme.colorScheme.onBackground
////                    ) } })
////    } }



//// CODE 2
//package com.example.pudent
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.core.app.ActivityCompat
//import android.content.pm.PackageManager
//
//@Composable
//fun HomeScreen(navController: NavController, bleManager: BleManager) {
//    val context = LocalContext.current
//    val discoveredDevices by bleManager.discoveredDevices.collectAsState()
//    val connectionState by remember { derivedStateOf { bleManager.connectionState.name } }
//    val isScanning by remember { derivedStateOf { bleManager.isScanning } }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Button(
//            onClick = {
//                if (!isScanning) bleManager.startScan()
//                else bleManager.stopScan()
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = if (isScanning) "Stop Scan" else "Start Scan")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Connection Status: $connectionState",
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(discoveredDevices) { device ->
//                val deviceName = try {
//                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                        device.name ?: "Unknown Device"
//                    } else {
//                        "Permission Required"
//                    }
//                } catch (e: SecurityException) {
//                    "Permission Denied"
//                }
//
//                val deviceAddress = try {
//                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                        device.address
//                    } else {
//                        "Permission Required"
//                    }
//                } catch (e: SecurityException) {
//                    "Permission Denied"
//                }
//
//                ListItem(
//                    headlineContent = { Text(deviceName) },
//                    supportingContent = { Text(deviceAddress) },
//                    trailingContent = {
//                        Button(
//                            onClick = { bleManager.connectToDevice(device) },
//                            enabled = bleManager.connectionState == BleManager.BleConnectionState.DISCONNECTED
//                        ) {
//                            Text("Connect")
//                        }
//                    }
//                )
//                Divider()
//            }
//        }
//
//        Button(
//            onClick = { bleManager.disconnectDevice() },
//            enabled = bleManager.connectionState == BleManager.BleConnectionState.CONNECTED,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        ) {
//            Text("Disconnect")
//        }
//    }
//}
//
//






//CODE 3
//package com.example.pudent
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import androidx.navigation.NavController
//
//@RequiresApi(Build.VERSION_CODES.S)
//@Composable
//fun HomeScreen(navController: NavController, bleManager: BleManager) {
//    val context = LocalContext.current
//    val discoveredDevices by bleManager.discoveredDevices.collectAsState()
//    val connectionState by remember { derivedStateOf { bleManager.connectionState } }
//    val isScanning by remember { derivedStateOf { bleManager.isScanning } }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Button(
//            onClick = {
//                if (!isScanning) {
//                    bleManager.startScan()
//                } else {
//                    bleManager.stopScan()
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = if (isScanning) "Stop Scan" else "Start Scan")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Connection Status: ${connectionState.name}",
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(discoveredDevices) { device ->
//                val deviceName = if (ActivityCompat.checkSelfPermission(
//                        context, Manifest.permission.BLUETOOTH_CONNECT
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    device.name ?: "Unknown Device"
//                } else {
//                    "Permission Required"
//                }
//
//                val deviceAddress = if (ActivityCompat.checkSelfPermission(
//                        context, Manifest.permission.BLUETOOTH_CONNECT
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    device.address
//                } else {
//                    "Permission Required"
//                }
//
//                ListItem(
//                    headlineContent = { Text(deviceName) },
//                    supportingContent = { Text(deviceAddress) },
//                    trailingContent = {
//                        Button(
//                            onClick = { bleManager.connectToDevice(device) },
//                            enabled = connectionState == BleManager.BleConnectionState.DISCONNECTED
//                        ) {
//                            Text("Connect")
//                        }
//                    }
//                )
//                Divider()
//            }
//        }
//
//        Button(
//            onClick = { bleManager.disconnectDevice() },
//            enabled = connectionState == BleManager.BleConnectionState.CONNECTED,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        ) {
//            Text("Disconnect")
//        }
//    }
//}



// CODE 4
//package com.example.pudent
//
//import android.Manifest
//import android.bluetooth.BluetoothDevice
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.provider.Settings
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Bluetooth
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.core.content.ContextCompat
//import android.content.pm.PackageManager
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.lazy.items
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HomeScreen(
//    navController: NavController,
//    bleManager: BleManager
//) {
//    val context = LocalContext.current
//    val discoveredDevices by bleManager.discoveredDevices.collectAsState()
//    val connectionState by remember { derivedStateOf { bleManager.connectionState } }
//    val isScanning by remember { derivedStateOf { bleManager.isScanning } }
//    val isAdvertising by remember { derivedStateOf { bleManager.isAdvertising } }
//
//    // Request Permission Launcher for Android 12+ (BLUETOOTH_CONNECT)
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (!isGranted) {
//            Toast.makeText(context, "Bluetooth permission is required to display device names.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
//            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
//        ) {
//            permissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Bluetooth and Location Quick Settings
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            IconButton(
//                onClick = { openBluetoothSettings(context) },
//                modifier = Modifier.size(64.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Bluetooth,
//                    contentDescription = "Bluetooth Settings",
//                    modifier = Modifier.size(48.dp)
//                )
//            }
//
//            IconButton(
//                onClick = { openLocationSettings(context) },
//                modifier = Modifier.size(64.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.LocationOn,
//                    contentDescription = "Location Settings",
//                    modifier = Modifier.size(48.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Scanning and Advertising Controls
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(
//                onClick = {
//                    if (!isScanning) bleManager.startScan()
//                    else bleManager.stopScan()
//                }
//            ) {
//                Text(if (isScanning) "Stop Scan" else "Start Scan")
//            }
//
//            Button(
//                onClick = {
//                    if (!isAdvertising) bleManager.startAdvertising()
//                    else bleManager.stopAdvertising()
//                }
//            ) {
//                Text(if (isAdvertising) "Stop Adv" else "Start Adv")
//            }
//        }
//
//        // Device List
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp)
//        ) {
//            items(discoveredDevices) { device ->
//                DisplayDevice(device = device, context = context)
//                Divider()
//            }
//        }
//
//        // Connection Status
//        Text(
//            text = "Status: ${connectionState.name}",
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Button(
//            onClick = { bleManager.disconnectDevice() },
//            enabled = connectionState == BleManager.BleConnectionState.CONNECTED
//        ) {
//            Text("Disconnect")
//        }
//    }
//}
//
//// Helper functions to open system settings
//fun openBluetoothSettings(context: Context) {
//    val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
//    context.startActivity(intent)
//}
//
//fun openLocationSettings(context: Context) {
//    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//    context.startActivity(intent)
//}
//
//// Composable to display each device safely
//@Composable
//fun DisplayDevice(device: BluetoothDevice, context: Context) {
//    val deviceName = remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                device.name ?: "Unknown Device"
//            } else {
//                "Permission Required"
//            }
//        } else {
//            device.name ?: "Unknown Device"
//        }
//    }
//
//    Column {
//        Text(text = deviceName)
//        Text(text = device.address)
//    }
//}







// CODE 5
//package com.example.pudent
//
//import android.Manifest
//import android.bluetooth.BluetoothAdapter
//import android.content.pm.PackageManager
//import android.os.Build
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import androidx.navigation.NavController
//
//@RequiresApi(Build.VERSION_CODES.S)
//@Composable
//fun HomeScreen(
//    navController: NavController,
//    bleManager: BleManager
//) {
//    val context = LocalContext.current
//
//    // States
//    val connectionState by bleManager.connectionState.collectAsState()
//    val isBluetoothEnabled = remember { bleManager.bluetoothAdapter?.let { mutableStateOf(it.isEnabled) } }
//
//    // Permissions launcher
//    val requestPermissionsLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val allGranted = permissions.values.all { it }
//        if (!allGranted) {
//            Toast.makeText(context, "Permissions denied. Some features may not work.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Request permissions on startup
//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            requestPermissionsLauncher.launch(
//                arrayOf(
//                    Manifest.permission.BLUETOOTH_SCAN,
//                    Manifest.permission.BLUETOOTH_CONNECT,
//                    Manifest.permission.BLUETOOTH_ADVERTISE
//                )
//            )
//        } else {
//            requestPermissionsLauncher.launch(
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
//            )
//        }
//    }
//
//    // UI
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Bluetooth State
//        if (isBluetoothEnabled != null) {
//            Text(
//                text = if (isBluetoothEnabled.value) "Bluetooth is Enabled" else "Bluetooth is Disabled",
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Toggle Bluetooth Button
//        Button(onClick = {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                val bluetoothAdapter = bleManager.bluetoothAdapter
//                if (bluetoothAdapter != null) {
//                    if (bluetoothAdapter.isEnabled) {
//                        bluetoothAdapter.disable()
//                        if (isBluetoothEnabled != null) {
//                            isBluetoothEnabled.value = false
//                        }
//                    } else {
//                        bluetoothAdapter.enable()
//                        if (isBluetoothEnabled != null) {
//                            isBluetoothEnabled.value = true
//                        }
//                    }
//                }
//            } else {
//                Toast.makeText(context, "Bluetooth connect permission required", Toast.LENGTH_SHORT).show()
//            }
//        }) {
//            Text("Toggle Bluetooth")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Disconnect Button
//        Button(
//            onClick = { bleManager.disconnectDevice() },
//            enabled = connectionState == BleManager.BleConnectionState.CONNECTED
//        ) {
//            Text("Disconnect Device")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display Connected Device Name
//        Text(
//            text = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                bleManager.connectedDevice?.name ?: "Unknown Device"
//            } else {
//                "Permission Required"
//            },
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Connection State
//        Text("Connection State: ${connectionState.name}", style = MaterialTheme.typography.bodyMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Example Connect Button (you can add actual BLE connect logic here)
//        Button(onClick = { /* Handle connection logic */ }) {
//            Text("Connect Device")
//        }
//    }
//}



//CODE 6
package com.example.pudent

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(
    navController: NavController,
    bleManager: BleManager
) {
    var isScanning by remember { mutableStateOf(false) }
    var discoveredDevices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }
    var connectionState by remember { mutableStateOf(ConnectionState.DISCONNECTED) }

    // Observe connection state from BleManager
    connectionState = bleManager.connectionState.value

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bluetooth Connection Status: ${connectionState.name}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isScanning) {
                    bleManager.stopScanning()
                } else {
                    bleManager.startScanning { devices ->
                        discoveredDevices = devices
                    }
                }
                isScanning = !isScanning
            }
        ) {
            Text(if (isScanning) "Stop Scanning" else "Start Scanning")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the list of discovered devices
        discoveredDevices.forEach { device ->
            Button(
                onClick = {
                    if (checkBluetoothPermission(context)) {
                        bleManager.connectToDevice(device)
                    } else {
                        // Handle the case where Bluetooth permissions are missing
                        // You can request the user to grant permission
                    }
                }
            ) {
                Text("Connect to ${device.name ?: "Unknown Device"}")
            }
        }
    }
}

fun checkBluetoothPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Bluetooth permissions are not needed for devices below Android S
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewHomeScreen() {
    val context = LocalContext.current
    val bleManager = BleManager(context)
    HomeScreen(navController = NavController(context), bleManager = bleManager)
}
