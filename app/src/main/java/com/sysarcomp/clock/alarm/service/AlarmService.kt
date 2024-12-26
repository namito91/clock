package com.sysarcomp.clock.alarm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.sysarcomp.clock.R
import com.sysarcomp.clock.alarm.model.AlarmReceiver


class AlarmService : Service() {

    private var ringtone: Ringtone? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("AlarmService", "on start command.")

        if (ringtone == null) { // Evitar múltiples instancias
            val alarmaUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ringtone = RingtoneManager.getRingtone(this, alarmaUri)
            ringtone?.isLooping = true
            ringtone?.play()
            Log.d("AlarmService", "Ringtone iniciado.")
        }

        createNotificationChannel()

        // Mostrar una notificación para que el usuario pueda detener la alarma
        showNotification()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ringtone?.isPlaying == true) {
            ringtone?.stop() // Detener el ringtone si está sonando
        }
        ringtone = null // Liberar el recurso
        Log.d("AlarmService", "Ringtone detenido y servicio destruido.")

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showNotification() {
        val stopIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = "STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "alarm_channel")
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarma Activa")
            .setContentText("Toque para detener")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Evita que se pueda deslizar la notificación
            .addAction(R.drawable.ic_stop, "Detener", stopPendingIntent)
            .build()

        Log.d("AlarmService", "Iniciando startForeground.")

        startForeground(1, notification)
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "alarm_channel",
            "Alarm Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para notificaciones de alarma"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
        Log.d("AlarmService", "Canal de notificación creado.")
    }
}


