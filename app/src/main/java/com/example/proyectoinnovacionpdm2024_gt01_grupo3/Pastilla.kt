package com.example.proyectoinnovacionpdm2024_gt01_grupo3

data class Pill(
    val id: Long = 0,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val frequency: Int,
    val startTime: String
)