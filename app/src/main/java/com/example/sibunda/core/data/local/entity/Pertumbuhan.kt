package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pertumbuhan")
data class Pertumbuhan(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val balitaId: Int,

    val nama: String,

    val umur: Int,

    val berat: Double,

    val tinggi: Double,

    val statusgizi: String,

    val tanggal: Long = System.currentTimeMillis()
)