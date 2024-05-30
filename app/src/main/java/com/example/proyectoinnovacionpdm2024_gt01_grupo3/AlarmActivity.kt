package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

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

        // Obtener una instancia del Vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Iniciar la vibración (pautas de vibración en milisegundos)
        val pattern = longArrayOf(0, 1000, 500) // Esperar 0ms, vibrar 1000ms, esperar 500ms
        vibrator.vibrate(pattern, 0) // Repetir la vibración desde el inicio del patrón (índice 0)

        buttonStop.setOnClickListener {
            // Detener el sonido de la alarma
            mediaPlayer.stop()
            mediaPlayer.release()
            // Detener la vibración
            vibrator.cancel()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        vibrator.cancel()
    }
}
