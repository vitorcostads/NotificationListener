package com.example.notificationlistener.core

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.notificationlistener.BuildConfig


class GastosNotificationListener : NotificationListenerService() {

    private val client = OkHttpClient()


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val pack = sbn?.packageName ?: return

        // 🔹 Lista de apps que o listener vai monitorar
        val appsPermitidos = listOf(
            "com.nu.production",          // Nubank
            "br.com.intermedium",         // Banco Inter
            "com.discord"                 // Discord utilizado para testes, remover no futuro
        )

        // 🔹 Verifica se o app está na lista
        if (!appsPermitidos.contains(pack)) {
            //Log.i("GASTOS", "Ignorado pacote: $pack") Evitar muitos logs
            return
        }

        // 🔹 Continua o fluxo normal
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        Log.i("GASTOS", "Notificação recebida de: $pack")
        Log.i("GASTOS", "Título: $title | Texto: $text")

        enviarParaApi(pack, title, text, sbn.postTime)
    }

    private fun enviarParaApi(pack: String, title: String, text: String, postTime: Long) {
        Thread {
            try {

                val postTimeIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    .format(Date(postTime))

                val json = JSONObject().apply {
                    put("app", pack)
                    put("titulo", title)
                    put("mensagem", text)
                    put("data", postTimeIso)
                }



                val body = json.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(BuildConfig.API_URL) // teu endpoint real
                    .post(body)
                    .build()

                Log.i("GASTOS", "Enviando JSON para API: $json")

                val response = client.newCall(request).execute()
                Log.i("GASTOS", "Resposta HTTP: ${response.code}")
                Log.i("GASTOS", "Corpo da resposta: ${response.body?.string()}")

                response.close()

            } catch (e: Exception) {
                Log.e("GASTOS", "Erro ao enviar para API: ${e::class.java.name} - ${e.message}", e)
            }
        }.start()
    }

}
