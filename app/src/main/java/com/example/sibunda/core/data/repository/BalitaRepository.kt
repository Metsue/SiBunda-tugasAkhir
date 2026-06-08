package com.example.sibunda.core.data.repository

import com.example.sibunda.core.data.local.entity.*
import com.example.sibunda.core.data.local.room.BalitaDao
import kotlinx.coroutines.flow.Flow

class BalitaRepository(
    private val dao: BalitaDao
) {

    suspend fun tambahData(
        namaIbu: String,
        namaAnak: String,
        umur: Int,
        berat: Double,
        tinggi: Double,
        status: String,
        tanggalPeriksa: Long
    ) {
        val existingIbu = dao.getIbuByNama(namaIbu)
        val ibuId = existingIbu?.id ?: dao.insertIbu(Ibu(namaIbu = namaIbu)).toInt()

        val existingBalita = dao.getBalitaByNama(namaAnak, ibuId)

        val balitaId = if (existingBalita != null) {
            dao.insertBalita(
                existingBalita.copy(
                    umur = umur,
                    berat = berat,
                    tinggi = tinggi,
                    statusgizi = status,
                    tanggal = tanggalPeriksa
                )
            )
            existingBalita.id
        } else {
            dao.insertBalita(
                Balita(
                    ibuId = ibuId,
                    nama = namaAnak,
                    umur = umur,
                    berat = berat,
                    tinggi = tinggi,
                    statusgizi = status,
                    tanggal = tanggalPeriksa
                )
            ).toInt()
        }

        dao.insertPertumbuhan(
            Pertumbuhan(
                balitaId = balitaId,
                nama = namaAnak,
                umur = umur,
                berat = berat,
                tinggi = tinggi,
                statusgizi = status,
                tanggal = tanggalPeriksa
            )
        )
    }

    fun getBalitaByIbu(ibuId: Int): Flow<List<Balita>> {
        return dao.getBalitaByIbu(ibuId)
    }

    fun getBalitaByMotherName(namaIbu: String): Flow<List<Balita>> {
        return dao.getBalitaByMotherName(namaIbu)
    }

    fun searchBalita(query: String): Flow<List<Balita>> {
        return dao.searchBalita("%$query%")
    }

    fun getRiwayatPertumbuhan(balitaId: Int): Flow<List<Pertumbuhan>> {
        return dao.getRiwayatPertumbuhan(balitaId)
    }
}