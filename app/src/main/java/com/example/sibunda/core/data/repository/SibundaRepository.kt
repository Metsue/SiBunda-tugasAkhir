package com.example.sibunda.core.data.repository

import com.example.sibunda.core.data.local.entity.AgendaEntity
import com.example.sibunda.core.data.local.entity.ChatEntity
import com.example.sibunda.core.data.local.room.SibundaDao
import kotlinx.coroutines.flow.Flow

class SibundaRepository(private val dao: SibundaDao) {
    fun getAgendas(): Flow<List<AgendaEntity>> = dao.getAllAgendas()
    
    fun getChats(): Flow<List<ChatEntity>> = dao.getChats()
    
    suspend fun sendChat(chat: ChatEntity) {
        dao.insertChat(chat)
    }
}
