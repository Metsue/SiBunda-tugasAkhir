package com.example.sibunda.ui.gizi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GiziViewModel : ViewModel() {
    private val _hasilBmi = MutableLiveData<Double>()
    val hasilBmi: LiveData<Double> = _hasilBmi

    private val _statusGizi = MutableLiveData<String>()
    val statusGizi: LiveData<String> = _statusGizi

    fun hitungStatusGizi(berat: Double, tinggiCm: Double) {
        val tinggiMeter = tinggiCm / 100
        val bmi = berat / (tinggiMeter * tinggiMeter)
        _hasilBmi.value = bmi

        // Klasifikasi sederhana sesuai standar kesehatan
        _statusGizi.value = when {
            bmi < 17.0 -> "Sangat Kurus (Gizi Buruk)"
            bmi in 17.0..18.4 -> "Kurus (Gizi Kurang)"
            bmi in 18.5..25.0 -> "Normal"
            else -> "Obesitas"
        }
    }
}