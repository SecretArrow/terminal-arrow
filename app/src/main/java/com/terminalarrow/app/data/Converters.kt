package com.terminalarrow.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): List<ForwardingRule> {
        val listType = object : TypeToken<List<ForwardingRule>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<ForwardingRule>): String {
        return Gson().toJson(list)
    }
}
