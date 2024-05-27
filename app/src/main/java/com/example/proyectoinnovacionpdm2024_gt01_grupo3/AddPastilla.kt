package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddPastilla : AppCompatActivity() {

    private lateinit var dbHelper: ConexionDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pastilla)

        dbHelper = ConexionDataBaseHelper(this)

        val editTextName = findViewById<TextInputEditText>(R.id.editTextName)
        val editTextDescription = findViewById<TextInputEditText>(R.id.editTextDescription)
        val editTextStartDate = findViewById<TextInputEditText>(R.id.editTextStartDate)
        val editTextEndDate = findViewById<TextInputEditText>(R.id.editTextEndDate)
        val editTextFrequency = findViewById<TextInputEditText>(R.id.editTextFrequency)
        val editTextStartTime = findViewById<TextInputEditText>(R.id.editTextStartTime)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        val calendar = Calendar.getInstance()

        editTextStartDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    editTextStartDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        editTextEndDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    editTextEndDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val startDate = editTextStartDate.text.toString()
            val endDate = editTextEndDate.text.toString()
            val frequency = editTextFrequency.text.toString().toInt()
            val startTime = editTextStartTime.text.toString()

            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(ConexionDataBaseHelper.COLUMN_NAME, name)
                put(ConexionDataBaseHelper.COLUMN_DESCRIPTION, description)
                put(ConexionDataBaseHelper.COLUMN_START_DATE, startDate)
                put(ConexionDataBaseHelper.COLUMN_END_DATE, endDate)
                put(ConexionDataBaseHelper.COLUMN_FREQUENCY, frequency)
                put(ConexionDataBaseHelper.COLUMN_START_TIME, startTime)
            }

            val newRowId = db?.insert(ConexionDataBaseHelper.TABLE_PILLS, null, values)

            if (newRowId != -1L) {
                Toast.makeText(this, "Pastilla Guardada: $newRowId", Toast.LENGTH_SHORT).show()
                setAlarm(name, description, frequency, startTime)
                finish()
            } else {
                Toast.makeText(this, "Error al Guardar la Pastilla", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAlarm(pillName: String, pillDescription: String, frequency: Int, startTime: String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, PastillaReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val startTimeParts = startTime.split(":").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            val now = Calendar.getInstance()
            val startHour = startTimeParts[0]
            val startMinute = startTimeParts[1]

            // Calcular la hora y los minutos para la notificación
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)

            // Verificar si la hora especificada ya ha pasado hoy
            if (now.after(this)) {
                // Si ha pasado, programar para el día siguiente
                add(Calendar.DATE, 1)
            } else {
                // Retroceder un minuto
                add(Calendar.MINUTE, -2)
            }

            // Convertir la frecuencia de horas a milisegundos
            val interval = frequency * 60 * 60 * 1000L

            // Programar la notificación con intervalo de repetición
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                interval,
                pendingIntent
            )
        }
    }
}
