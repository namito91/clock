package com.sysarcomp.clock.alarm.data

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sysarcomp.clock.alarm.model.UserAlarm

fun saveAlarms(context: Context, userAlarms: List<UserAlarm>) {

    val sharedPreferences = context.getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Convierte la lista a JSON
    val gson = Gson()
    val json = gson.toJson(userAlarms)

    editor.putString("userAlarms", json)
    editor.apply()
}


fun loadAlarms(context: Context): MutableList<UserAlarm> {

    val sharedPreferences = context.getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
    val gson = Gson()

    val json = sharedPreferences.getString("userAlarms", null)
    val type = object : TypeToken<MutableList<UserAlarm>>() {}.type

    return if (json != null) {
        gson.fromJson(json, type)
    } else {
        mutableListOf()
    }
}
