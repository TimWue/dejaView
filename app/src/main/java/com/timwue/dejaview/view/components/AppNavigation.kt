package com.timwue.dejaview.view.components

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timwue.dejaview.dao.AppDatabase
import com.timwue.dejaview.model.Device

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyAppNavHost(
    database: AppDatabase,
    devices : List<Device>, location : Location?,
    startBLEScan: ()->Unit, stopBLEScan: ()->Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "live-view"
) {
    fun seenRecently(device: Device):Boolean{
        val currentTimestamp = System.currentTimeMillis()
        val FIFTEEN_MINUTES = 900000L
        return currentTimestamp - device.lastSeen < FIFTEEN_MINUTES

    }

    Scaffold(bottomBar = { Navbar(navController)}) { contentPadding ->
        NavHost(
            modifier = modifier.padding(contentPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            composable("live-view") {
                LiveView(startBLEScan,stopBLEScan,devices.filter { seenRecently(it) }, location)
            }
            composable("history-view") {
                HistoryView(database)
            }
            }
        }
    }


@Composable
fun Navbar(navController: NavHostController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.End)
    ){
        Button(modifier = Modifier.padding(5.dp) ,onClick = { navController.navigate("live-view") }) {
            Text(text = "Live")
        }
        Button(modifier = Modifier.padding(5.dp), onClick = { navController.navigate("history-view") }) {
            Text(text = "History")
        }
    }
}