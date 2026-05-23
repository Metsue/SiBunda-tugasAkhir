package com.example.sibunda.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sibunda.core.data.local.entity.Balita
import kotlinx.coroutines.flow.Flow

@Dao
interface BalitaDao {
    @Query("SELECT * FROM balita")
    fun getAllBalita(): Flow<List<Balita>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalita(balita: Balita)

    @Query("SELECT * FROM balita WHERE nama LIKE :query")
    fun searchBalitaByNama(query: String): Flow<List<Balita>>
}
