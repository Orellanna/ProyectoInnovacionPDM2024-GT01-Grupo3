package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton


class DetallePastilla : AppCompatActivity() {
    private lateinit var dbHelper: ConexionDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pastilla)

        dbHelper = ConexionDataBaseHelper(this)

        // Obtener los datos pasados a través del Intent
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val frequency = intent.getIntExtra("frequency", 0)
        val startTime = intent.getStringExtra("startTime")
        val pillId = intent.getLongExtra("PILL_ID", -1)

        // Referenciar los TextViews del layout
        val nameTextView = findViewById<TextView>(R.id.textViewNameDetail)
        val descriptionTextView = findViewById<TextView>(R.id.textViewDescriptionDetail)
        val datesTextView = findViewById<TextView>(R.id.textViewDatesDetail)
        val frequencyTextView = findViewById<TextView>(R.id.textViewFrequencyDetail)
        val startTimeTextView = findViewById<TextView>(R.id.textViewStartTimeDetail)

        // Referenciar el botón de eliminar
        val buttonDelete = findViewById<MaterialButton>(R.id.buttonDelete)

        // Asignar los valores a los TextViews
        nameTextView.text = name
        descriptionTextView.text = description
        datesTextView.text = "Desde $startDate hasta $endDate"
        frequencyTextView.text = "Cada $frequency horas"
        startTimeTextView.text = "Hora de inicio: $startTime"

        // Agregar OnClickListener al botón de eliminar
        buttonDelete.setOnClickListener {
            if (pillId != -1L) {
                // Eliminar la pastilla de la base de datos
                dbHelper.deletePill(pillId.toInt())
                // Finalizar la actividad actual
                finish()
            } else {
                // ID de pastilla no válido, mostrar un mensaje de error o manejar el caso según corresponda
            }
        }
    }
}

