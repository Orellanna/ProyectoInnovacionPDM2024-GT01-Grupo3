package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallePastilla : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pastilla)

        // Obtener los datos pasados a través del Intent
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val frequency = intent.getIntExtra("frequency", 0)
        val startTime = intent.getStringExtra("startTime")

        // Referenciar los TextViews del layout
        val nameTextView = findViewById<TextView>(R.id.textViewNameDetail)
        val descriptionTextView = findViewById<TextView>(R.id.textViewDescriptionDetail)
        val datesTextView = findViewById<TextView>(R.id.textViewDatesDetail)
        val frequencyTextView = findViewById<TextView>(R.id.textViewFrequencyDetail)
        val startTimeTextView = findViewById<TextView>(R.id.textViewStartTimeDetail)

        // Asignar los valores a los TextViews
        nameTextView.text = name
        descriptionTextView.text = description
        datesTextView.text = "Desde $startDate hasta $endDate"
        frequencyTextView.text = "Cada $frequency horas"
        startTimeTextView.text = "Hora de inicio: $startTime"
    }
}
