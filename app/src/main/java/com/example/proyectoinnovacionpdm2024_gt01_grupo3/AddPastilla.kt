package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddPastilla : AppCompatActivity() {

    private lateinit var dbHelper: ConexionDataBaseHelper
    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pastilla)

        dbHelper = ConexionDataBaseHelper(this)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        editTextStartDate = findViewById(R.id.editTextStartDate)
        editTextEndDate = findViewById(R.id.editTextEndDate)
        val editTextFrequency = findViewById<EditText>(R.id.editTextFrequency)
        val editTextStartTime = findViewById<EditText>(R.id.editTextStartTime)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        editTextStartDate.setOnClickListener {
            showDatePickerDialog(editTextStartDate)
        }

        editTextEndDate.setOnClickListener {
            showDatePickerDialog(editTextEndDate)
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
                Toast.makeText(this, "Pastilla Guardada Id: $newRowId", Toast.LENGTH_SHORT).show()
                setAlarm(name, description, frequency, startTime)
                finish()
            } else {
                Toast.makeText(this, "Error al guardar la Pastilla", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateDateInView(editText)
        }

        DatePickerDialog(
            this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView(editText: EditText) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editText.setText(sdf.format(calendar.time))
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(pillName: String, pillDescription: String, frequency: Int, startTime: String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val startTimeParts = startTime.split(":").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            val now = Calendar.getInstance()
            val startHour = startTimeParts[0]
            val startMinute = startTimeParts[1]

            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)

            if (now.after(this)) {
                add(Calendar.DATE, 1)
            }
        }

        // Intent and PendingIntent for the notification
        val notificationIntent = Intent(this, PastillaReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", calendar.timeInMillis)
        }
        val notificationPendingIntent = PendingIntent.getBroadcast(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Intent and PendingIntent for the alarm
        val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("pill_name", pillName)
            putExtra("pill_description", pillDescription)
            putExtra("frequency", frequency)
            putExtra("alarm_time", calendar.timeInMillis)
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Schedule the first notification 2 minutes before the start time
        val notificationTime = calendar.timeInMillis - 2 * 60 * 1000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, notificationPendingIntent)

        // Schedule the first alarm at the start time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent)
    }



}

