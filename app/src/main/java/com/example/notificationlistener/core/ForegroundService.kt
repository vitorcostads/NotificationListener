package com.example.notificationlistener.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.notificationlistener.R

class ForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "gastos_listener_channel"
        val channelName = "Gastos Listener Ativo"

        try {
            val manager = getSystemService(NotificationManager::class.java)
            val chan = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(chan)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Gastos ativo")
                .setContentText("Monitorando notificações em segundo plano")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

            startForeground(1, notification)
            Log.i("GASTOS", "ForegroundService iniciado com sucesso")

        } catch (e: Exception) {
            Log.e("GASTOS", "Erro ao iniciar ForegroundService: ${e.message}", e)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null
}
