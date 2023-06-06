package com.timwue.dejaview.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LongListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLongList(longList: List<Long>): String {
        return gson.toJson(longList)
    }

    @TypeConverter
    fun toLongList(longListString: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(longListString, type)
    }
}
