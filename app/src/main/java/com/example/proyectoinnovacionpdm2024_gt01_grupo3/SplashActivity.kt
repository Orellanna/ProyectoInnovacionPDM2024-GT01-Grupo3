package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Temporizador para mostrar la actividad durante 2 segundos
        Handler().postDelayed({
            // Abrir la MainActivity después de 2 segundos
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 2000 milisegundos = 2 segundos
    }
}
