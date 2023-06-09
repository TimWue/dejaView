package com.timwue.dejaview

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.location.*
import com.timwue.dejaview.dao.AppDatabase
import com.timwue.dejaview.model.Device
import com.timwue.dejaview.ui.theme.DejaviewTheme
import com.timwue.dejaview.usecase.checkPermissions
import com.timwue.dejaview.usecase.requestPermissions
import com.timwue.dejaview.view.components.MyAppNavHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var scanCallback: ScanCallback
    private lateinit var locationProvider : FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var devices = mutableMapOf<String, Device>()
    private var currentLocation = mutableStateOf<Location?>(null)
    private var PERMISSION_ID = 44
    private lateinit var db : AppDatabase;

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val deviceList = db.deviceDao().getAll()
                deviceList.forEach{
                    devices[it.id]= it
                }
            }
        }

        initializeLocationProvider()

        setContent {
            DejaviewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   MyAppNavHost(database = db, devices = devices.values.toList(), location = currentLocation.value, startBLEScan = {startBLEScan()}, stopBLEScan = {stopBLEScan()})
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    fun stopBLEScan(){
        if (checkPermissions(this)) {
            bluetoothLeScanner.stopScan(scanCallback)
        }
      saveDevices()
    }

    private fun saveDevices(){
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val deviceList = devices.values.toList().toTypedArray()
                db.deviceDao().insertAll(*deviceList)            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onPause() {
        super.onPause()
        saveDevices()
        locationProvider.removeLocationUpdates(locationCallback)
    }

    private fun initializeLocationProvider(){
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){
                    currentLocation.value = location
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun startBLEScan(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if (!bluetoothAdapter.isEnabled || !packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("Please enable Bluetooth.")
        }

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val currentTimestamp = System.currentTimeMillis()
                val deviceName = result.device.name ?: result.device.alias ?: "N/A"

                val deviceId = result.device.address
                val device = devices[deviceId] ?: Device(result.device.address, result.rssi, deviceName, currentTimestamp)

                if (device.meetTimes.isNotEmpty()) {

                    // if more than one hour away
                    if ((currentTimestamp - device.meetTimes.last()) > 3600000L ){
                        device.meetTimes = device.meetTimes.plus(currentTimestamp)
                    }
                } else {
                        device.meetTimes = device.meetTimes.plus(currentTimestamp)
                }

                device.lastSeen = currentTimestamp
                devices[device.id] = device
            }
        }

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val scanFilter = ScanFilter.Builder()
            // Add any desired filters here
            .build()

        if (checkPermissions(this)) {
            bluetoothLeScanner.startScan(listOf(scanFilter), scanSettings, scanCallback)
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (checkPermissions(this)) {
            if (isLocationEnabled()) {
                val locationRequest = LocationRequest
                    .Builder(Priority.PRIORITY_HIGH_ACCURACY,10000)
                    .build()

                locationProvider.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                showToast("Please turn on your location!")
            }
        } else {
            requestPermissions(this, PERMISSION_ID)
        }
    }

    private fun showToast(text : String){
        Toast.makeText(this, text, Toast.LENGTH_LONG)
            .show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}

