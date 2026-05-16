package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agenda_posyandu")
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val tanggal: String,
    val keterangan: String
)