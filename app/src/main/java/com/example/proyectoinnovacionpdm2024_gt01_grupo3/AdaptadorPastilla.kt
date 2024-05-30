package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
    }

    override fun getItemCount() = pills.size
}
