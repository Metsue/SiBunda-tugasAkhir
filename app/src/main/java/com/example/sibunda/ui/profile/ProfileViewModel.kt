package com.example.sibunda.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sibunda.core.utils.Constants

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

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

    private val _fotoProfile = MutableLiveData<String>()
    val fotoProfile: LiveData<String> = _fotoProfile

    fun loadUserProfile() {
        _namaIbu.value = sharedPreferences.getString(Constants.KEY_NAMA, "Bunda")
        _nikIbu.value = sharedPreferences.getString(Constants.KEY_NIK, "Belum diisi")
        _usernameIbu.value = sharedPreferences.getString(Constants.KEY_USERNAME, "Belum login")
        _teleponIbu.value = sharedPreferences.getString(Constants.KEY_TELEPON, "Belum diisi")
        _alamatIbu.value = sharedPreferences.getString(Constants.KEY_ALAMAT, "Belum diisi")
        _fotoProfile.value = sharedPreferences.getString(Constants.KEY_FOTO_PROFILE, "")
    }
}