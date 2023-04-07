package com.timwue.dejaview.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timwue.dejaview.MessageCard

data class Device(val id : String, val signalStrength: String, val name : String)

@Composable
fun Device(device: Device){
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
      Text(device.signalStrength, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun DeviceList(devices : List<Device>){
    LazyColumn(Modifier.fillMaxHeight()){
        items(devices){ device -> Device(device) }
    }
}

