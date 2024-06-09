package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class PastillaReceiver : BroadcastReceiver() {

    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"
        val frequency = intent.getIntExtra("frequency", 1)
        val alarmTime = intent.getLongExtra("alarm_time", System.currentTimeMillis())

        showNotification(context, pillName, pillDescription)
        reprogramNotification(context, pillName, pillDescription, frequency, alarmTime)
    }

    private fun showNotification(context: Context, pillName: String, pillDescription: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "pill_reminder_channel"
        val channelName = "Pill Reminder"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Channel for pill reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_pasti)
            .setContentTitle(pillName)
            .setContentText(pillDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun reprogramNotification(context: Context, pillName: String, pillDescription: String, frequency: Int, alarmTimeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val nextNotificationTimeInMillis = alarmTimeInMillis + frequency * 60 * 60 * 1000 - 2 * 60 * 1000

        val notificationIntent = Intent(context, PastillaReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", nextNotificationTimeInMillis + 2 * 60 * 1000) // Pass the correct alarm time
        }

        val notificationPendingIntent = PendingIntent.getBroadcast(
            context, nextNotificationTimeInMillis.toInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextNotificationTimeInMillis, notificationPendingIntent)
    }
}
