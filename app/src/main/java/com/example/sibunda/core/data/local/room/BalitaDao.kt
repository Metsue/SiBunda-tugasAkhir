package com.example.sibunda.core.data.local.room

import androidx.room.*
import com.example.sibunda.core.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BalitaDao {

    // ======================
    // IBU
    // ======================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIbu(ibu: Ibu): Long

    @Query("SELECT * FROM ibu WHERE namaIbu = :nama LIMIT 1")
    suspend fun getIbuByNama(nama: String): Ibu?

    // ======================
    // BALITA
    // ======================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalita(balita: Balita): Long

    @Query("SELECT * FROM balita WHERE ibuId = :ibuId ORDER BY nama ASC")
    fun getBalitaByIbu(ibuId: Int): Flow<List<Balita>>

    @Query("""
        SELECT b.* FROM balita b
        INNER JOIN ibu i ON b.ibuId = i.id
        WHERE i.namaIbu = :namaIbu
        ORDER BY b.nama ASC
    """)
    fun getBalitaByMotherName(namaIbu: String): Flow<List<Balita>>

    @Query("""
        SELECT b.* FROM balita b
        INNER JOIN ibu i ON b.ibuId = i.id
        WHERE i.namaIbu = :namaIbu AND b.nama LIKE :query
        ORDER BY b.nama ASC
    """)
    fun searchBalitaByMother(namaIbu: String, query: String): Flow<List<Balita>>

    @Query("SELECT * FROM balita WHERE nama LIKE :query")
    fun searchBalita(query: String): Flow<List<Balita>>

    @Query("SELECT * FROM balita WHERE nama = :nama AND ibuId = :ibuId LIMIT 1")
    suspend fun getBalitaByNama(nama: String, ibuId: Int): Balita?

    // ======================
    // PERTUMBUHAN
    // ======================

    @Insert
    suspend fun insertPertumbuhan(pertumbuhan: Pertumbuhan)

    @Query("SELECT * FROM pertumbuhan WHERE balitaId = :balitaId ORDER BY umur ASC")
    fun getRiwayatPertumbuhan(balitaId: Int): Flow<List<Pertumbuhan>>
}
