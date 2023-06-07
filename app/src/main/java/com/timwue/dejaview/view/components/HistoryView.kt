package com.timwue.dejaview.view.components

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.timwue.dejaview.dao.AppDatabase
import com.timwue.dejaview.model.Device

@Composable
fun HistoryView(database: AppDatabase, modifier: Modifier = Modifier){
    val devices: MutableState<List<Device>> = remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        devices.value  = database.deviceDao().getAll()
    }
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
            DeviceList(devices.value.sortedByDescending { device -> device.lastSeen })
        }
}

