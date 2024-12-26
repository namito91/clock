package com.sysarcomp.clock.alarm.logic

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.sysarcomp.clock.alarm.data.saveAlarms
import com.sysarcomp.clock.alarm.model.AlarmReceiver
import com.sysarcomp.clock.alarm.model.UserAlarm
import java.util.Calendar

@SuppressLint("ScheduleExactAlarm")
fun setAlarm(
    context: Context,
    hour: Int,
    minute: Int,
    userAlarms: MutableList<UserAlarm>,
    requestCode: Int
) {

    //Obtenemos el servicio del sistema para manejar alarmas
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //  Creamos un intent que será recibido por AlarmReceiver
    val intent = Intent(context, AlarmReceiver::class.java)

    // Una intent que será ejecutada por AlarmManager cuando llegue el momento.
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Usamos esta clase para establecer la hora de la alarma.
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0) // Asegúrate de reiniciar los segundos
        set(Calendar.MILLISECOND, 0) // Asegúrate de reiniciar los milisegundos

        // Ajusta para el siguiente día si la hora ya pasó
        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    //  Configura una alarma precisa que puede ejecutarse incluso si el dispositivo está en reposo.
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

    userAlarms.add(UserAlarm(requestCode, hour, minute))

    saveAlarms(context, userAlarms)

    Toast.makeText(context, "Alarma configurada para las $hour:$minute", Toast.LENGTH_SHORT).show()
}