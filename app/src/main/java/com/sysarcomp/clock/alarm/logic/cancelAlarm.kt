package com.sysarcomp.clock.alarm.logic

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sysarcomp.clock.alarm.model.AlarmReceiver
import com.sysarcomp.clock.alarm.model.UserAlarm


fun cancelAlarm(context: Context, requestCode: Int, userAlarms: MutableList<UserAlarm>,) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Crear el mismo Intent y PendingIntent con el requestCode único
    val intent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val userAlarm = userAlarms.find { alarm -> alarm.id == requestCode }

    userAlarms.remove(userAlarm)

    // Cancelar la alarma asociada a este PendingIntent
    alarmManager.cancel(pendingIntent)
}