package com.sysarcomp.clock.worldclock.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveDatetime(context: Context, datetime: List<String>) {
    val sharedPreferences = context.getSharedPreferences("DatetimePreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Convierte la lista a JSON
    val gson = Gson()
    val json = gson.toJson(datetime)

    editor.putString("datetime", json)
    editor.apply()
}


fun loadDatetime(context: Context): MutableList<String> {
    val sharedPreferences = context.getSharedPreferences("DatetimePreferences", Context.MODE_PRIVATE)
    val gson = Gson()

    val json = sharedPreferences.getString("datetime", null)
    val type = object : TypeToken<MutableList<String>>() {}.type

    return if (json != null) {
        gson.fromJson(json, type)
    } else {
        mutableListOf()
    }
}