package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar

class PastillaReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "pill_reminder_channel"
    private val NOTIFICATION_ID = 123

    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"

        val builder = NotificationCompat.Builder(context, "pill_reminder_channel")
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle(pillName)
            .setContentText(pillDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(123, builder.build())
        }
    }

}
