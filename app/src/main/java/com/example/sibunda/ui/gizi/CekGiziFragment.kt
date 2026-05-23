package com.example.sibunda.ui.gizi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.databinding.FragmentCekGiziBinding
import com.example.sibunda.ui.kelola_pertumbuhan.BalitaViewModel

class CekGiziFragment : Fragment() {
    private var _binding: FragmentCekGiziBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: BalitaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCekGiziBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHitung.setOnClickListener {
            simpandata()
        }
    }

    private fun simpandata() {
        val nama = binding.etNamaBalita.text.toString().trim()
        val umurstr = binding.etUmur.text.toString().trim()
        val beratstr = binding.etBerat.text.toString().trim()
        val tinggistr = binding.etTinggi.text.toString().trim()

        if (nama.isEmpty() || umurstr.isEmpty() || beratstr.isEmpty() || tinggistr.isEmpty()) {
            Toast.makeText(requireContext(), "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val umur = umurstr.toIntOrNull() ?: 0
        val berat = beratstr.toDoubleOrNull() ?: 0.0
        val tinggi = tinggistr.toDoubleOrNull() ?: 0.0

        val statusgizi = viewmodel.hitungStatusGizi(berat, umur)
        binding.tvHasil.text = "Status Gizi: $statusgizi"

        val balita = Balita(
            nama = nama,
            umur = umur,
            berat = berat,
            tinggi = tinggi,
            statusgizi = statusgizi
        )

        viewmodel.insert(balita)
        Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
