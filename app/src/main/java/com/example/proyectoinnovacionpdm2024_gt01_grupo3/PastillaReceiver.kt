package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar

class PastillaReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "pill_reminder_channel"

    @SuppressLint("ScheduleExactAlarm", "ServiceCast")
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"
        val frequency = intent.getIntExtra("frequency", 1) // Frequency in hours
        val alarmTime = intent.getLongExtra("alarm_time", System.currentTimeMillis())

        // Wake lock to keep the device awake while processing the notification
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PastillaReceiver::WakeLock")
        wakeLock.acquire(10*60*1000L /*10 minutes*/)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle(pillName)
            .setContentText(pillDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            notify(123, builder.build())
        }

        // Reprogramar la próxima notificación
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextNotificationIntent = Intent(context, PastillaReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", alarmTime + frequency * 3600000) // next alarm time in milliseconds
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 1, nextNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextNotificationTime = alarmTime + frequency * 3600000 - 2 * 60 * 1000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextNotificationTime, pendingIntent)

        wakeLock.release()
    }
}
