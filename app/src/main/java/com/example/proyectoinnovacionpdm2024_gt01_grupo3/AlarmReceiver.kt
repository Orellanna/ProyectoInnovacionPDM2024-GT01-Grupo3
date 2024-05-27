package com.example.proyectoinnovacionpdm2024_gt01_grupo3


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Obtener el nombre y la descripción de la pastilla del intent
        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de Tomar la Pastilla"

        // Crear un intent para iniciar la actividad de la alarma
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(alarmIntent)
    }
}
