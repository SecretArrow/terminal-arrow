package com.terminalarrow.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<ForwardingRule> {
        if (value == null) return emptyList()
        return try {
            val listType = object : TypeToken<List<ForwardingRule>>() {}.type
            Gson().fromJson(value, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromList(list: List<ForwardingRule>): String {
        return Gson().toJson(list)
    }
}
