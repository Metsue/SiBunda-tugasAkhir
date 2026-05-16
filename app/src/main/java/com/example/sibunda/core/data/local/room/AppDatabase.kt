package com.example.sibunda.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sibunda.core.data.local.entity.AgendaEntity
import com.example.sibunda.core.data.local.entity.BalitaEntity
import com.example.sibunda.core.data.local.entity.ChatEntity

@Database(
    entities = [
        BalitaEntity::class,
        AgendaEntity::class,
        ChatEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sibundaDao(): SibundaDao
}
