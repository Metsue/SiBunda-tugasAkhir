package com.example.sibunda.core.data.local.room

import androidx.room.*
import com.example.sibunda.core.data.local.entity.AgendaEntity
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.core.data.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SibundaDao {
    @Query("SELECT * FROM balita")
    fun getAllBalita(): Flow<List<Balita>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalita(balita: Balita)

    @Query("SELECT * FROM agenda_posyandu")
    fun getAllAgendas(): Flow<List<AgendaEntity>>

    @Query("SELECT * FROM chat_konsultasi")
    fun getChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)
}
