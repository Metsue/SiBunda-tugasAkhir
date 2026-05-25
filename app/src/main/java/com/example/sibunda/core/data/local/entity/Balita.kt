package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balita")
data class Balita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ibuId: Int,
    val nama: String,
    val umur: Int = 0,
    val berat: Double = 0.0,
    val tinggi: Double = 0.0,
    val statusgizi: String = "",
    val tanggal: Long = System.currentTimeMillis()
)
