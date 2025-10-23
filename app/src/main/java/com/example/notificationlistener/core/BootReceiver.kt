package com.example.notificationlistener.core

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("RECEIVER", "BootReceiver recebeu algo: ${intent?.action}")
        if (context == null || intent?.action == null) return

        val action = intent.action
        Log.i("GASTOS", "BootReceiver acionado com ação: $action")

        if (
            action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            action == "com.example.notificationlistener.TEST_BOOT"
        ) {
            try {
                // Reinicia o listener
                val cn = ComponentName(context, GastosNotificationListener::class.java)
                val pm = context.packageManager
                pm.setComponentEnabledSetting(
                    cn,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    cn,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )

                // Inicia o serviço em primeiro plano (para manter ativo)
                val serviceIntent = Intent(context, ForegroundService::class.java)
                context.startForegroundService(serviceIntent)

                Log.i("GASTOS", "Listener e serviço reiniciados com sucesso após $action")

            } catch (e: Exception) {
                Log.e("GASTOS", "Erro ao reiniciar listener no boot: ${e.message}", e)
            }
        }
    }
}
