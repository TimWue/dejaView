package com.timwue.dejaview.view.components

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timwue.dejaview.model.Device

@Composable
fun HistoryView(devices : List<Device>,modifier: Modifier = Modifier,){
      Column(modifier = modifier) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("History",style = MaterialTheme.typography.h1)
            }
            Spacer(modifier = Modifier.height(20.dp))
            DeviceList(devices.sortedByDescending { device -> device.lastSeen })
        }
}

