package com.timwue.dejaview.view.components

import android.bluetooth.le.ScanResult
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timwue.dejaview.model.Device
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Device(device: Device){

    var rssi = "N/A"
    if (device.rssi != ScanResult.TX_POWER_NOT_PRESENT &&
        device.rssi != Integer.MIN_VALUE){
        rssi = device.rssi.toString()
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
      Column {
          Text(text = device.name, style = MaterialTheme.typography.body1)
          Text(text = device.id, style = MaterialTheme.typography.body2)
      }
        Column(horizontalAlignment = Alignment.End) {
            Text(rssi, style = MaterialTheme.typography.body1, textAlign = TextAlign.End )
            Text(text = formatTime(device.lastSeen), style = MaterialTheme.typography.body2)
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(milliseconds)
    return sdf.format(date)
}

@Composable
fun DeviceList(devices : List<Device>){
    LazyColumn(Modifier.fillMaxHeight()){
        items(devices){ device -> Device(device) }
    }
}

