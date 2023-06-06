package com.timwue.dejaview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
    @PrimaryKey @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "rssi") val rssi: Int,
    @ColumnInfo(name = "name") val name: String
)