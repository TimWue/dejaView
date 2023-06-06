package com.timwue.dejaview.dao

import androidx.room.*
import com.timwue.dejaview.model.Device

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    suspend fun getAll(): List<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg devices: Device)

    @Delete
    fun delete(device: Device)
}