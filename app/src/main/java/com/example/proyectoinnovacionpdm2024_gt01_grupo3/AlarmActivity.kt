package com.example.proyectoinnovacionpdm2024_gt01_grupo3

// AlarmActivity.kt

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val pillName = intent.getStringExtra("pill_name") ?: "Pastilla"
        val pillDescription = intent.getStringExtra("pill_description") ?: "Hora de tomar la pastilla"

        val textViewAlarm = findViewById<TextView>(R.id.textViewAlarm)
        val buttonStop = findViewById<Button>(R.id.buttonStop)

        textViewAlarm.text = "$pillName - $pillDescription"

        // Iniciar la alarma sonora
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        buttonStop.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}

