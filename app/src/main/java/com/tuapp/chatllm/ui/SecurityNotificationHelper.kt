package com.tuapp.chatllm.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.tuapp.chatllm.R

object SecurityNotificationHelper {
    private const val CHANNEL_ID = "security_2fa_channel"
    private const val CHANNEL_NAME = "Notificaciones de Seguridad 2FA"

    fun send2FAEmailNotification(context: Context, userEmail: String, code: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para códigos de verificación 2FA por correo"
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (notificationManager != null) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle("📩 Correo de Verificación 2FA")
                .setContentText("Código para $userEmail: $code")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Hola. Se ha generado un nuevo código de seguridad en 2 pasos para tu cuenta ($userEmail).\n\nTu Código OTP es: $code\n\nIngrésalo en la aplicación para activar tu cuenta.")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            try {
                notificationManager.notify((100..9999).random(), builder.build())
            } catch (_: Throwable) {
            }
        }

        Toast.makeText(
            context,
            "📩 Notificación Enviada: Código 2FA es $code",
            Toast.LENGTH_LONG
        ).show()
    }

    fun openEmailApp(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_EMAIL)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Throwable) {
            Toast.makeText(context, "Abre tu aplicación de correo para revisar la notificación", Toast.LENGTH_SHORT).show()
        }
    }
}
