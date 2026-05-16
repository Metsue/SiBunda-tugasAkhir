package com.example.sibunda.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_konsultasi")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pesan: String,
    val isUser: Boolean, // True jika dari Bunda, False jika dari Ahli Gizi
    val timestamp: Long = System.currentTimeMillis()
)