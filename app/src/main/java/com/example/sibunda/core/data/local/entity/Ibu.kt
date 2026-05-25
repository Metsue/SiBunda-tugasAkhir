package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ibu")
data class Ibu(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val namaIbu: String
)