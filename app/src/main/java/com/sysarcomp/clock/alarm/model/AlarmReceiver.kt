package com.sysarcomp.clock.alarm.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sysarcomp.clock.alarm.service.AlarmService

// BroadcastReceiver: Permite que tu app escuche y responda a eventos, en este caso, la alarma.

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action

        if (action == "STOP_ALARM") {
            Log.d("AlarmReceiver", "Acción recibida: STOP_ALARM")
            // Detener el servicio AlarmService
            context?.stopService(Intent(context, AlarmService::class.java))
        } else {
            // Otra lógica, como iniciar la alarma
            val serviceIntent = Intent(context, AlarmService::class.java)
            context?.stopService(serviceIntent) // Asegúrate de detener cualquier instancia anterior
            context?.startForegroundService(serviceIntent)

        }
    }
}



