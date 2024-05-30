package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Vibrator
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"
        val frequency = intent.getIntExtra("frequency", 1) // Frequency in hours

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
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, nextAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextAlarmTime = Calendar.getInstance().apply {
            add(Calendar.HOUR, frequency)
        }.timeInMillis

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime, pendingIntent)
    }
}
