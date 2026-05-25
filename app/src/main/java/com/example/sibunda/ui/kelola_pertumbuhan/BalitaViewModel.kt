package com.example.sibunda.ui.kelola_pertumbuhan

import android.app.Application
import androidx.lifecycle.*
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.core.data.local.entity.Pertumbuhan
import com.example.sibunda.core.data.local.room.AppDatabase
import com.example.sibunda.core.data.repository.BalitaRepository
import kotlinx.coroutines.launch

class BalitaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BalitaRepository

    private val searchQuery = MutableLiveData("")

    private val selectedBalitaId = MutableLiveData<Int>()

    init {
        val dao = AppDatabase.getDatabase(application).balitaDao()
        repository = BalitaRepository(dao)
    }

    // SEARCH BALITA
    val searchResults: LiveData<List<Balita>> =
        searchQuery.switchMap { query ->
            repository.searchBalita(query).asLiveData()
        }

    // DATA BALITA BERDASARKAN IBU
    fun getBalitaByMother(namaIbu: String): LiveData<List<Balita>> {
        return repository.getBalitaByMotherName(namaIbu).asLiveData()
    }

    // PILIH BALITA UNTUK CHART
    fun selectBalita(id: Int) {
        selectedBalitaId.value = id
    }

    // RIWAYAT PERTUMBUHAN
    val riwayatPertumbuhan: LiveData<List<Pertumbuhan>> =
        selectedBalitaId.switchMap { id ->
            repository.getRiwayatPertumbuhan(id).asLiveData()
        }

    fun getRiwayatPertumbuhan(balitaId: Int)
            : LiveData<List<Pertumbuhan>> {
        return repository.getRiwayatPertumbuhan(balitaId).asLiveData()
    }

    // TAMBAH DATA
    fun tambahData(
        namaIbu: String,
        namaAnak: String,
        umur: Int,
        berat: Double,
        tinggi: Double,
        status: String
    ) = viewModelScope.launch {

        repository.tambahData(
            namaIbu,
            namaAnak,
            umur,
            berat,
            tinggi,
            status
        )
    }

    // SEARCH
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    // HITUNG STATUS GIZI
    fun hitungStatusGizi(
        berat: Double,
        umur: Int
    ): String {

        val idealMin = 2.0 + (umur * 0.5)
        val idealMax = 4.0 + (umur * 0.5)

        return when {
            berat < idealMin -> "Gizi Kurang"
            berat > idealMax -> "Gizi Lebih"
            else -> "Gizi Normal"
        }
    }
}