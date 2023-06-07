package com.timwue.dejaview.view.components

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GpsLocation(location : Location?){
    var latString = "N/A"
    var lonString = "N/A"

    if (location !== null){
        latString = location.latitude.toString()
        lonString = location.longitude.toString()
    }

    Column{
        Text(
            "Latitude: $latString",
            Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.End,
        )
        Text(
            "Longitude: $lonString",
            Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.End
        )
    }

}