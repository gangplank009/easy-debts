package com.android.gangplank.easydebts.room.entities

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}