package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 123
    private lateinit var recyclerView: RecyclerView
    private lateinit var pillAdapter: PillAdapter
    private lateinit var dbHelper: ConexionDataBaseHelper
    private val CHANNEL_ID = "pill_reminder_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        requestPermission()

        createNotificationChannel()

        dbHelper = ConexionDataBaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddPastilla::class.java)
            startActivity(intent)
        }

        loadPills()

    }

    private fun loadPills() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            ConexionDataBaseHelper.COLUMN_ID,
            ConexionDataBaseHelper.COLUMN_NAME,
            ConexionDataBaseHelper.COLUMN_DESCRIPTION,
            ConexionDataBaseHelper.COLUMN_START_DATE,
            ConexionDataBaseHelper.COLUMN_END_DATE,
            ConexionDataBaseHelper.COLUMN_FREQUENCY,
            ConexionDataBaseHelper.COLUMN_START_TIME
        )

        val cursor = db.query(
            ConexionDataBaseHelper.TABLE_PILLS,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val pills = mutableListOf<Pill>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_NAME))
                val description = getString(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_DESCRIPTION))
                val startDate = getString(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_START_DATE))
                val endDate = getString(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_END_DATE))
                val frequency = getInt(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_FREQUENCY))
                val startTime = getString(getColumnIndexOrThrow(ConexionDataBaseHelper.COLUMN_START_TIME))
                pills.add(Pill(id, name, description, startDate, endDate, frequency, startTime))
            }
        }

        pillAdapter = PillAdapter(pills)
        recyclerView.adapter = pillAdapter
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel == null) {
                val name = "Pill Reminder"
                val descriptionText = "Channel for Pill Reminder notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso concedido
            } else {
                // Permiso denegado, manejar el caso
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadPills()
    }
}
