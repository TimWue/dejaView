package com.timwue.dejaview.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timwue.dejaview.model.Device

@Database(entities = [Device::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}