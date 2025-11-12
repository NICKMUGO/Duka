package com.example.duka.dao.data

import androidx.room.TypeConverter
import java.util.Date

//This file acts as a translator for Room to understand the data types
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}