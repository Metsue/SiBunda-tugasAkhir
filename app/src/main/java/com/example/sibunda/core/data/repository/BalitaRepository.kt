package com.example.sibunda.core.data.repository

import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.core.data.local.room.BalitaDao
import kotlinx.coroutines.flow.Flow

class BalitaRepository(private val balitaDao: BalitaDao) {
    val allBalita: Flow<List<Balita>> = balitaDao.getAllBalita()

    suspend fun insert(balita: Balita) {
        balitaDao.insertBalita(balita)
    }

    fun search(query: String): Flow<List<Balita>> {
        return balitaDao.searchBalitaByNama("%$query%")
    }
}
