package com.timwue.dejaview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.timwue.dejaview.dao.LongListConverter

@Entity
@TypeConverters(LongListConverter::class)
data class Device(
    @PrimaryKey @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "rssi") val rssi: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lastSeen") val lastSeen: Long,
    @ColumnInfo(name = "meetTimes") val meetTimes: List<Long> = emptyList()
)