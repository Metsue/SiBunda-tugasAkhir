package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balita")
data class Balita (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val umur: Int,
    val berat: Double,
    val tinggi: Double,
    val statusgizi: String,
    val tanggal: Long = System.currentTimeMillis()
)
