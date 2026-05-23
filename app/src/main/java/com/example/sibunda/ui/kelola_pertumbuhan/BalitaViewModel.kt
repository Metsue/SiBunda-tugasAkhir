package com.example.sibunda.ui.kelola_pertumbuhan

import android.app.Application
import androidx.lifecycle.*
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.core.data.local.room.AppDatabase
import com.example.sibunda.core.data.repository.BalitaRepository
import kotlinx.coroutines.launch

class BalitaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BalitaRepository
    val allBalitaData: LiveData<List<Balita>>
    private val searchQuery = MutableLiveData<String>("")

    init {
        val dao = AppDatabase.getDatabase(application).balitaDao()
        repository = BalitaRepository(dao)
        allBalitaData = repository.allBalita.asLiveData()
    }

    val searchResults: LiveData<List<Balita>> = searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.allBalita.asLiveData()
        } else {
            repository.search(query).asLiveData()
        }
    }

    fun insert(balita: Balita) = viewModelScope.launch {
        repository.insert(balita)
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun hitungStatusGizi(berat: Double, umur: Int): String {
        val idealMin = 2.0 + (umur * 0.5)
        val idealMax = 4.0 + (umur * 0.5)
        return when {
            berat < idealMin -> "Gizi Kurang"
            berat > idealMax -> "Gizi Lebih"
            else -> "Gizi Normal"
        }
    }
}
