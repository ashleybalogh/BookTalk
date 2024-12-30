package com.booktalk.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.booktalk.domain.model.book.ReadingStatus

object Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromReadingStatus(value: ReadingStatus): String {
        return value.name
    }

    @TypeConverter
    fun toReadingStatus(value: String): ReadingStatus {
        return ReadingStatus.valueOf(value)
    }
}
