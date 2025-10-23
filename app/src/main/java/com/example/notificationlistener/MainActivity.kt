package com.example.notificationlistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.ComponentName
import android.content.pm.PackageManager
import android.util.Log
import com.example.notificationlistener.core.GastosNotificationListener
import com.example.notificationlistener.ui.theme.NotificationListenerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, com.example.notificationlistener.core.ForegroundService::class.java)
        startForegroundService(serviceIntent)

        // 🔹 FORÇA REBIND DO LISTENER AO ABRIR O APP
        val cn = ComponentName(this, GastosNotificationListener::class.java)
        val enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")


        if (enabledListeners == null || !enabledListeners.contains(cn.flattenToString())) {
            Log.i("GASTOS", "Listener ainda não autorizado.")
        } else {
            try {
                Log.i("GASTOS", "Reiniciando listener (rebind automático)...")
                val pkgManager = packageManager
                pkgManager.setComponentEnabledSetting(
                    cn,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pkgManager.setComponentEnabledSetting(
                    cn,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                Log.e("GASTOS", "Erro ao reiniciar listener: ${e.message}", e)
            }
        }

        // 🔹 INTERFACE PADRÃO
        setContent {
            NotificationListenerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionScreen()
                }
            }
        }
    }
}

@Composable
fun PermissionScreen() {
    var status by remember { mutableStateOf("Permissão ainda não verificada") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Acesso a notificações", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = status)
        Spacer(modifier = Modifier.height(24.dp))
        val context = LocalContext.current
        Button(onClick = {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            context.startActivity(intent)
        }) {
            Text("Abrir configurações")
        }
    }
}
