package com.example.sibunda.ui.gizi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentCekGiziBinding
import com.example.sibunda.ui.kelola_pertumbuhan.BalitaViewModel

class CekGiziFragment : Fragment() {
    private var _binding: FragmentCekGiziBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: BalitaViewModel by viewModels()
    private var listBalita: List<Balita> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCekGiziBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        val namaIbu = sharedPref.getString("KEY_USERNAME", "Bunda") ?: "Bunda"

        setupBalitaSelection(namaIbu)

        binding.btnHitung.setOnClickListener {
            simpandata(namaIbu)
        }

        binding.btnReset.setOnClickListener {
            resetFields()
        }
    }

    private fun setupBalitaSelection(namaIbu: String) {
        viewmodel.getBalitaByMother(namaIbu).observe(viewLifecycleOwner) { data ->
            listBalita = data
            val names = data.map { it.nama }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names)
            binding.acNamaBalita.setAdapter(adapter)
        }

        binding.acNamaBalita.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position) as String
            val balita = listBalita.find { it.nama == selectedName }
            balita?.let {
                binding.etUmur.setText(it.umur.toString())
                binding.etBerat.setText(it.berat.toString())
                binding.etTinggi.setText(it.tinggi.toString())
                binding.tvHasil.text = "Data terakhir ditemukan.\nStatus Gizi Terakhir: ${it.statusgizi}"
            }
        }
    }

    private fun resetFields() {
        binding.acNamaBalita.setText("")
        binding.etUmur.setText("")
        binding.etBerat.setText("")
        binding.etTinggi.setText("")
        binding.tvHasil.text = "Mode: Tambah Anak Baru"
        binding.acNamaBalita.requestFocus()
    }

    private fun simpandata(namaIbu: String) {
        val nama = binding.acNamaBalita.text.toString().trim()
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

        viewmodel.tambahData(
            namaIbu = namaIbu,
            namaAnak = nama,
            umur = umur,
            berat = berat,
            tinggi = tinggi,
            status = statusgizi
        )

        Toast.makeText(requireContext(), "Data berhasil disinkronkan ke Kelola Pertumbuhan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
