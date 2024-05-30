package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.PowerManager
import android.os.Vibrator
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"
        val frequency = intent.getIntExtra("frequency", 1) // Frequency in hours
        val alarmTime = intent.getLongExtra("alarm_time", System.currentTimeMillis())

        // Wake lock to keep the device awake while processing the alarm
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver::WakeLock")
        wakeLock.acquire(10*60*1000L /*10 minutes*/)

        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
        }
        context.startActivity(alarmIntent)

        // Reprogramar la próxima alarma
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextAlarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", alarmTime + frequency * 3600000) // next alarm time in milliseconds
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, nextAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextAlarmTime = alarmTime + frequency * 3600000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime, pendingIntent)

        wakeLock.release()
    }
}
