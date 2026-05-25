package com.example.sibunda.core.domain.model

data class Balita(
    val id: Int = 0,
    val namaIbu: String,
    val namaAnak: String,
    val umur: Int,
    val berat: Double,
    val tinggi: Double,
    val status: String
)