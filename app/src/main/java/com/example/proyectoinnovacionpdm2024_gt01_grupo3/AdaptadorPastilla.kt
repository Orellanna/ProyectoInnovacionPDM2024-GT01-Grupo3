package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PillAdapter(private val pills: List<Pill>) : RecyclerView.Adapter<PillAdapter.PillViewHolder>() {

    class PillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val datesTextView: TextView = itemView.findViewById(R.id.textViewDates)
        val frequencyTextView: TextView = itemView.findViewById(R.id.textViewFrequency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pill_item, parent, false)
        return PillViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        val currentPill = pills[position]
        holder.nameTextView.text = currentPill.name
        holder.descriptionTextView.text = currentPill.description
        holder.datesTextView.text = "Desde ${currentPill.startDate} hasta ${currentPill.endDate}"
        holder.frequencyTextView.text = "Cada ${currentPill.frequency} horas desde las: ${currentPill.startTime}"

        // Añadir OnClickListener para lanzar la nueva actividad con los detalles
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallePastilla::class.java).apply {
                putExtra("name", currentPill.name)
                putExtra("description", currentPill.description)
                putExtra("startDate", currentPill.startDate)
                putExtra("endDate", currentPill.endDate)
                putExtra("frequency", currentPill.frequency)
                putExtra("startTime", currentPill.startTime)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = pills.size
}


