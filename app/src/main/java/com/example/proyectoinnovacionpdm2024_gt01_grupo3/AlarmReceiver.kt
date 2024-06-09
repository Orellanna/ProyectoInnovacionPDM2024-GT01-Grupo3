package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"
        val frequency = intent.getIntExtra("frequency", 1)
        val alarmTime = intent.getLongExtra("alarm_time", System.currentTimeMillis())

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver::WakeLock")
        wakeLock.acquire(10 * 60 * 1000L /*10 minutos*/)

        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
        }

        context.startActivity(alarmIntent)
        wakeLock.release()

        reprogramAlarm(context, pillName, pillDescription, frequency, alarmTime)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun reprogramAlarm(context: Context, pillName: String, pillDescription: String, frequency: Int, alarmTimeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val nextAlarmTimeInMillis = alarmTimeInMillis + frequency * 60 * 60 * 1000

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", nextAlarmTimeInMillis)
        }

        val alarmPendingIntent = PendingIntent.getBroadcast(
            context, (nextAlarmTimeInMillis + 1).toInt(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTimeInMillis, alarmPendingIntent)

        // Schedule the next notification 2 minutes before the next alarm
        val nextNotificationTimeInMillis = nextAlarmTimeInMillis - 2 * 60 * 1000

        val notificationIntent = Intent(context, PastillaReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", nextAlarmTimeInMillis)
        }

        val notificationPendingIntent = PendingIntent.getBroadcast(
            context, nextNotificationTimeInMillis.toInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextNotificationTimeInMillis, notificationPendingIntent)
    }
}
