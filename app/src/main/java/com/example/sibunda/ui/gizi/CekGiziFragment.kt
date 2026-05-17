package com.example.sibunda.ui.gizi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentCekGiziBinding

class GiziFragment : Fragment() {

    private var _binding: FragmentCekGiziBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCekGiziBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHitung.setOnClickListener {
            hitungGizi()
        }
    }

    private fun hitungGizi() {
        val nama = binding.etNamaBalita.text.toString().trim()
        val umurText = binding.etUmur.text.toString().trim()
        val beratText = binding.etBerat.text.toString().trim()
        val tinggiText = binding.etTinggi.text.toString().trim()

        if (nama.isEmpty() || umurText.isEmpty() || beratText.isEmpty() || tinggiText.isEmpty()) {
            Toast.makeText(context, "Harap isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val umur = umurText.toIntOrNull()
        val berat = beratText.toDoubleOrNull()
        val tinggi = tinggiText.toDoubleOrNull()

        if (umur == null || berat == null || tinggi == null) {
            Toast.makeText(context, "Data umur, berat, dan tinggi harus berupa angka", Toast.LENGTH_SHORT).show()
            return
        }

        val statusGizi = when {
            berat < 10 -> "Gizi Kurang"
            berat in 10.0..15.0 -> "Gizi Normal"
            else -> "Gizi Berlebih"
        }

        val hasil = """
           Nama Balita: $nama
           Umur: $umur bulan
           Berat Badan: $berat kg
           Tinggi Badan: $tinggi cm
          
           Status Gizi: $statusGizi
       """.trimIndent()

        binding.tvHasil.text = hasil
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
