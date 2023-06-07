package com.timwue.dejaview.view.components

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timwue.dejaview.model.Device

@Composable
fun LiveView(startBLEScan: ()->Unit, stopBLEScan: ()->Unit,devices : List<Device>, location : Location?,modifier: Modifier = Modifier,){
    val tag = "LIVE-VIEW"

    LaunchedEffect(Unit) {
        Log.d(tag, "Launching ...")
        startBLEScan()
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.d(tag, "Disposing ...")
            stopBLEScan()
        }
    }
        Column(modifier = modifier) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Live View",style = MaterialTheme.typography.h1)
                GpsLocation(location)
            }
            Spacer(modifier = Modifier.height(20.dp))

            DeviceList(devices.sortedByDescending { device -> device.lastSeen })
        }
}

