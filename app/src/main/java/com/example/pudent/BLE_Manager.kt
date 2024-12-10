
// CODE 3 working last
//package com.example.pudent
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.*
//import android.bluetooth.le.BluetoothLeScanner
//import android.bluetooth.le.ScanCallback
//import android.bluetooth.le.ScanResult
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.*
//import androidx.core.app.ActivityCompat
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//class BleManager(private val context: Context) {
//    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
//    private var bluetoothLeScanner: BluetoothLeScanner? = null
//    private var currentGatt: BluetoothGatt? = null
//
//    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
//    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices.asStateFlow()
//
//    var connectionState by mutableStateOf(BleConnectionState.DISCONNECTED)
//        private set
//
//    var isScanning by mutableStateOf(false)
//        private set
//
//    private val SCAN_PERIOD: Long = 10000
//
//    private val scanCallback = object : ScanCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            val device = result.device
//            _discoveredDevices.update { devices ->
//                if (devices.none { it.address == device.address }) devices + device else devices
//            }
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            isScanning = false
//            Log.e("BleManager", "Scan failed with error code: $errorCode")
//            Toast.makeText(context, "Bluetooth scan failed", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private val gattCallback = object : BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            Log.d("BleManager", "Connection status: $status, New state: $newState")
//
//            if (status != BluetoothGatt.GATT_SUCCESS) {
//                Log.e("BleManager", "Connection error: $status")
//                Toast.makeText(context, "Connection failed: $status", Toast.LENGTH_SHORT).show()
//                connectionState = BleConnectionState.DISCONNECTED
//                return
//            }
//
//            when (newState) {
//                BluetoothProfile.STATE_CONNECTED -> {
//                    connectionState = BleConnectionState.CONNECTED
//                    gatt.discoverServices()
//                }
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    connectionState = BleConnectionState.DISCONNECTED
//                    gatt.close()
//                }
//            }
//        }
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d("BleManager", "Services discovered successfully")
//            } else {
//                Log.e("BleManager", "Service discovery failed: $status")
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun startScan() {
//        if (bluetoothAdapter == null) {
//            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (!bluetoothAdapter.isEnabled) {
//            enableBluetoothManually()
//            return
//        }
//
//        if (!hasRequiredPermissions()) {
//            Toast.makeText(context, "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
//        _discoveredDevices.value = emptyList()
//        isScanning = true
//        bluetoothLeScanner?.startScan(scanCallback)
//
//        Handler(Looper.getMainLooper()).postDelayed({ stopScan() }, SCAN_PERIOD)
//    }
//
//    @SuppressLint("MissingPermission")
//    fun stopScan() {
//        if (!hasRequiredPermissions()) return
//
//        bluetoothLeScanner?.stopScan(scanCallback)
//        isScanning = false
//    }
//
//    @SuppressLint("MissingPermission")
//    fun connectToDevice(device: BluetoothDevice) {
//        if (!hasRequiredPermissions()) {
//            Toast.makeText(context, "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        currentGatt = device.connectGatt(context, false, gattCallback)
//        connectionState = BleConnectionState.CONNECTING
//    }
//
//    @SuppressLint("MissingPermission")
//    fun disconnectDevice() {
//        currentGatt?.let {
//            it.disconnect()
//            it.close()
//        }
//        currentGatt = null
//        connectionState = BleConnectionState.DISCONNECTED
//    }
//
//    private fun hasRequiredPermissions(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//        } else {
//            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun enableBluetoothManually() {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    Toast.makeText(context, "Bluetooth connect permission required", Toast.LENGTH_SHORT).show()
//                    return
//                }
//            }
//
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(enableBtIntent)
//        } catch (e: SecurityException) {
//            Log.e("BleManager", "Failed to enable Bluetooth", e)
//            Toast.makeText(context, "Cannot enable Bluetooth", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    enum class BleConnectionState {
//        DISCONNECTED, CONNECTING, CONNECTED
//    }
//}

// CODE 4
//package com.example.pudent
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.*
//import android.bluetooth.le.*
//import android.content.Context
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.ParcelUuid
//import android.util.Log
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.*
//import androidx.core.app.ActivityCompat
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import java.util.*
//
//class BleManager(private val context: Context) {
//    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
//    private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
//    private var currentGatt: BluetoothGatt? = null
//    private var advertisingSet: AdvertisingSet? = null
//
//    private val SERVICE_UUID = UUID.fromString("00000001-0000-1000-8000-00805f9b34fb")
//    private val CHARACTERISTIC_UUID = UUID.fromString("00000002-0000-1000-8000-00805f9b34fb")
//
//    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
//    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices.asStateFlow()
//
//    var connectionState by mutableStateOf(BleConnectionState.DISCONNECTED)
//        private set
//
//    var isScanning by mutableStateOf(false)
//        private set
//
//    var isAdvertising by mutableStateOf(false)
//        private set
//
//    private val SCAN_PERIOD: Long = 10000
//
//    private val scanCallback = object : ScanCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            val device = result.device
//            _discoveredDevices.update { devices ->
//                if (devices.none { it.address == device.address }) {
//                    devices + device
//                } else devices
//            }
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            isScanning = false
//            Log.e("BleManager", "Scan failed: $errorCode")
//            Toast.makeText(context, "Scan failed: $errorCode", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private val gattCallback = object : BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            when (newState) {
//                BluetoothProfile.STATE_CONNECTED -> {
//                    connectionState = BleConnectionState.CONNECTED
//                    gatt.discoverServices()
//                }
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    connectionState = BleConnectionState.DISCONNECTED
//                    gatt.close()
//                }
//            }
//        }
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                val service = gatt.getService(SERVICE_UUID)
//                val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)
//                // Add custom logic for characteristic if required
//            }
//        }
//    }
//
//    // AdvertiseCallback to handle advertising events
//    private val advertiseCallback = object : AdvertiseCallback() {
//        // Corrected method signature
//        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
//            super.onStartSuccess(settingsInEffect)
//            Log.i("BleManager", "Advertising started successfully")
//            isAdvertising = true
//        }
//
//        override fun onStartFailure(errorCode: Int) {
//            super.onStartFailure(errorCode)
//            Log.e("BleManager", "Advertising failed with error code: $errorCode")
//            isAdvertising = false
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun startScan() {
//        if (!checkPermission(Manifest.permission.BLUETOOTH_SCAN) || bluetoothAdapter == null) {
//            Toast.makeText(context, "Bluetooth not ready or missing permission", Toast.LENGTH_SHORT).show()
//            return
//        }
//        isScanning = true
//        bluetoothLeScanner?.startScan(scanCallback)
//    }
//
//    fun stopScan() {
//        if (!checkPermission(Manifest.permission.BLUETOOTH_SCAN)) {
//            Log.e("BleManager", "Permission BLUETOOTH_SCAN not granted")
//            return
//        }
//        bluetoothLeScanner?.stopScan(scanCallback)
//        isScanning = false
//    }
//
//    @SuppressLint("MissingPermission")
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun startAdvertising() {
//        if (!checkPermission(Manifest.permission.BLUETOOTH_ADVERTISE) || bluetoothAdapter == null) {
//            Toast.makeText(context, "Bluetooth not ready or missing permission", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val advertiser = bluetoothAdapter.bluetoothLeAdvertiser
//        val parameters = AdvertiseSettings.Builder()
//            .setConnectable(false)
//            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
//            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
//            .build()
//
//        val data = AdvertiseData.Builder()
//            .addServiceUuid(ParcelUuid(SERVICE_UUID))
//            .build()
//
//        advertiser?.startAdvertising(parameters, data, advertiseCallback)
//    }
//
//    fun stopAdvertising() {
//        if (!checkPermission(Manifest.permission.BLUETOOTH_ADVERTISE)) {
//            Log.e("BleManager", "Permission BLUETOOTH_ADVERTISE not granted")
//            return
//        }
//        val advertiser = bluetoothAdapter?.bluetoothLeAdvertiser
//        advertiser?.stopAdvertising(advertiseCallback)
//        isAdvertising = false
//    }
//
//    @SuppressLint("MissingPermission")
//    fun disconnectDevice() {
//        if (!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
//            Log.e("BleManager", "Permission BLUETOOTH_CONNECT not granted")
//            return
//        }
//        try {
//            currentGatt?.disconnect()
//            currentGatt?.close()
//            connectionState = BleConnectionState.DISCONNECTED
//        } catch (e: SecurityException) {
//            Log.e("BleManager", "SecurityException while disconnecting device: ${e.message}")
//        }
//    }
//
//    private fun checkPermission(permission: String): Boolean {
//        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    enum class BleConnectionState { DISCONNECTED, CONNECTING, CONNECTED }
//}




// CODE 5
//package com.example.pudent
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.*
//import android.bluetooth.le.*
//import android.content.Context
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Looper
//import android.os.Handler
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.mutableStateOf
//import androidx.core.app.ActivityCompat
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import java.util.*
//
//class BleManager(private val context: Context) {
//
//    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
//    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
//    private val bluetoothLeAdvertiser: BluetoothLeAdvertiser? = bluetoothAdapter?.bluetoothLeAdvertiser
//
//    private var bluetoothGatt: BluetoothGatt? = null
//
//    private val _connectionState = MutableStateFlow(BleConnectionState.DISCONNECTED)
//    val connectionState: StateFlow<BleConnectionState> = _connectionState.asStateFlow()
//
//    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
//    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices.asStateFlow()
//
//    private val _isScanning = MutableStateFlow(false)
//    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
//
//    var connectedDevice: BluetoothDevice? = null
//        private set
//
//    private val scanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            val device = result.device
//            _discoveredDevices.update { devices ->
//                if (devices.none { it.address == device.address }) devices + device else devices
//            }
//        }
//
//        override fun onBatchScanResults(results: MutableList<ScanResult>) {
//            _discoveredDevices.value = results.map { it.device }
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            _isScanning.value = false
//        }
//    }
//
//    private val gattCallback = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            _connectionState.value = when (newState) {
//                BluetoothProfile.STATE_CONNECTED -> {
//                    if (ActivityCompat.checkSelfPermission(
//                            context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        gatt.discoverServices() // Call discoverServices if permission is granted
//                        BleConnectionState.CONNECTED
//                    } else {
//                        // Handle the case when permission is not granted
//                        BleConnectionState.DISCONNECTED
//                    }
//                }
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    connectedDevice = null
//                    BleConnectionState.DISCONNECTED
//                }
//                BluetoothProfile.STATE_CONNECTING -> BleConnectionState.CONNECTING
//                else -> _connectionState.value
//            }
//        }
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                // Handle services discovery here if needed
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun startScan() {
//        if (_isScanning.value) return
//
//        bluetoothLeScanner?.startScan(scanCallback)
//        _isScanning.value = true
//
//        // Stop scanning after 10 seconds
//        Handler(Looper.getMainLooper()).postDelayed({
//            stopScan()
//        }, 10_000)
//    }
//
//    @SuppressLint("MissingPermission")
//    fun stopScan() {
//        if (!_isScanning.value) return
//
//        bluetoothLeScanner?.stopScan(scanCallback)
//        _isScanning.value = false
//    }
//
//    @SuppressLint("MissingPermission")
//    fun connectToDevice(device: BluetoothDevice) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            _connectionState.value = BleConnectionState.CONNECTING
//            bluetoothGatt = device.connectGatt(context, false, gattCallback)
//            connectedDevice = device
//        } else {
//            Toast.makeText(context, "Bluetooth connect permission required", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun disconnectDevice() {
//        bluetoothGatt?.disconnect()
//        bluetoothGatt?.close()
//        bluetoothGatt = null
//        connectedDevice = null
//        _connectionState.value = BleConnectionState.DISCONNECTED
//    }
//
//    enum class BleConnectionState {
//        DISCONNECTED, CONNECTING, CONNECTED
//    }
//}

//CODE 6
package com.example.pudent

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat

class BleManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    var connectionState = mutableStateOf(ConnectionState.DISCONNECTED)
    private var bluetoothGatt: BluetoothGatt? = null

    // Check if the required permissions are granted
    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Start scanning for Bluetooth devices
    @RequiresApi(Build.VERSION_CODES.S)
    fun startScanning(onDevicesFound: (List<BluetoothDevice>) -> Unit) {
        if (hasBluetoothPermissions()) {
            try {
                bluetoothLeScanner?.startScan(object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult?) {
                        result?.device?.let {
                            // Notify the UI with the found device
                            onDevicesFound(listOf(it))  // Add the device to the list (you can refine this logic)
                        }
                    }

                    override fun onScanFailed(errorCode: Int) {
                        // Handle scan failure
                    }
                })
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            // Request permissions if not granted
        }
    }

    // Stop scanning for Bluetooth devices
    @RequiresApi(Build.VERSION_CODES.S)
    fun stopScanning() {
        // Check if Bluetooth permissions are granted
        if (hasBluetoothPermissions()) {
            try {
                bluetoothLeScanner?.stopScan(scanCallback)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            // Handle case where permissions are missing, you can prompt the user to grant permissions
        }
    }

    // Example method to connect to a Bluetooth device
    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToDevice(device: BluetoothDevice) {
        if (hasBluetoothPermissions()) {
            try {
                connectionState.value = ConnectionState.CONNECTING
                bluetoothGatt = device.connectGatt(context, false, gattCallback)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            // Handle the case where permissions are missing
        }
    }

    // Sample scan callback to handle found devices
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let {
                // Add discovered device
            }
        }

        override fun onScanFailed(errorCode: Int) {
            // Handle scan failure
        }
    }

    // Example GATT callback for connection state changes
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                connectionState.value = ConnectionState.CONNECTED
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                connectionState.value = ConnectionState.DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            // Handle services discovery
        }
    }
}

enum class ConnectionState {
    CONNECTED, DISCONNECTED, CONNECTING
}
