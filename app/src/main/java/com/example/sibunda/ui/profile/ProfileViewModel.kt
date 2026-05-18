package com.example.sibunda.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sibunda.core.utils.Constants

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    private val _namaIbu = MutableLiveData<String>()
    val namaIbu: LiveData<String> = _namaIbu

    private val _nikIbu = MutableLiveData<String>()
    val nikIbu: LiveData<String> = _nikIbu

    private val _usernameIbu = MutableLiveData<String>()
    val usernameIbu: LiveData<String> = _usernameIbu

    private val _teleponIbu = MutableLiveData<String>()
    val teleponIbu: LiveData<String> = _teleponIbu

    private val _alamatIbu = MutableLiveData<String>()
    val alamatIbu: LiveData<String> = _alamatIbu

    // Fungsi untuk memuat data yang tersinkronisasi dari session login/register
    fun loadUserProfile() {
        _namaIbu.value = sharedPreferences.getString("KEY_NAMA", "Bunda Prans")
        _nikIbu.value = sharedPreferences.getString("KEY_NIK", "6371XXXXXXXXXXXX")
        _usernameIbu.value = sharedPreferences.getString("KEY_USERNAME", "bunda.prans@email.com")
        _teleponIbu.value = sharedPreferences.getString("KEY_TELEPON", "08XXXXXXXXXX")
        _alamatIbu.value = sharedPreferences.getString("KEY_ALAMAT", "Kota Banjarmasin, Kalimantan Selatan")
    }
}