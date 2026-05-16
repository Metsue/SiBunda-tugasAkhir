package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balita_table")
data class BalitaEntity(
    @PrimaryKey val id: String,
    val nama: String,
    val umurBulan: Int,
    val beratBadan: Double,
    val tinggiBadan: Double,
    val jenisKelamin: String
)
