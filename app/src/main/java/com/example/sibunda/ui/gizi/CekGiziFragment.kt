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
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CekGiziFragment : Fragment() {

    private var _binding: FragmentCekGiziBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: BalitaViewModel by viewModels()
    private var listBalita: List<Balita> = emptyList()

    private var tanggalPeriksaMillis: Long = System.currentTimeMillis()

    private val formatTanggal = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

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

        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val namaIbu = sharedPref.getString(Constants.KEY_NAMA, "Bunda") ?: "Bunda"

        setTanggalHariIni()
        setupDatePicker()
        setupBalitaSelection(namaIbu)

        binding.btnHitung.setOnClickListener {
            simpandata(namaIbu)
        }

        binding.btnReset.setOnClickListener {
            resetFields()
        }
    }

    private fun setTanggalHariIni() {
        tanggalPeriksaMillis = System.currentTimeMillis()
        binding.etTanggalPeriksa.setText(formatTanggal.format(Date(tanggalPeriksaMillis)))
    }

    private fun setupDatePicker() {
        binding.etTanggalPeriksa.setOnClickListener {
            tampilkanDatePicker()
        }

        binding.etTanggalPeriksa.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tampilkanDatePicker()
            }
        }
    }

    private fun tampilkanDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal Pemeriksaan")
            .setSelection(tanggalPeriksaMillis)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            tanggalPeriksaMillis = selection
            binding.etTanggalPeriksa.setText(formatTanggal.format(Date(selection)))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER_PEMERIKSAAN")
    }

    private fun setupBalitaSelection(namaIbu: String) {
        viewmodel.getBalitaByMother(namaIbu).observe(viewLifecycleOwner) { data ->
            listBalita = data
            val names = data.map { it.nama }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                names
            )
            binding.acNamaBalita.setAdapter(adapter)
        }

        binding.acNamaBalita.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position) as String
            val balita = listBalita.find { it.nama == selectedName }

            balita?.let {
                tanggalPeriksaMillis = it.tanggal
                binding.etTanggalPeriksa.setText(formatTanggal.format(Date(it.tanggal)))
                binding.etUmur.setText(it.umur.toString())
                binding.etBerat.setText(it.berat.toString())
                binding.etTinggi.setText(it.tinggi.toString())
                binding.tvHasil.text =
                    "Data terakhir ditemukan.\nTanggal: ${formatTanggal.format(Date(it.tanggal))}\nStatus Gizi Terakhir: ${it.statusgizi}"
            }
        }
    }

    private fun resetFields() {
        binding.acNamaBalita.setText("")
        binding.etUmur.setText("")
        binding.etBerat.setText("")
        binding.etTinggi.setText("")
        setTanggalHariIni()
        binding.tvHasil.text = "Mode: Tambah Anak Baru"
        binding.acNamaBalita.requestFocus()
    }

    private fun simpandata(namaIbu: String) {
        val nama = binding.acNamaBalita.text.toString().trim()
        val umurstr = binding.etUmur.text.toString().trim()
        val beratstr = binding.etBerat.text.toString().trim()
        val tinggistr = binding.etTinggi.text.toString().trim()
        val tanggalStr = binding.etTanggalPeriksa.text.toString().trim()

        if (
            nama.isEmpty() ||
            umurstr.isEmpty() ||
            beratstr.isEmpty() ||
            tinggistr.isEmpty() ||
            tanggalStr.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val umur = umurstr.toIntOrNull()
        val berat = beratstr.toDoubleOrNull()
        val tinggi = tinggistr.toDoubleOrNull()

        if (umur == null || umur <= 0) {
            binding.etUmur.error = "Umur tidak valid"
            binding.etUmur.requestFocus()
            return
        }

        if (berat == null || berat <= 0.0) {
            binding.etBerat.error = "Berat badan tidak valid"
            binding.etBerat.requestFocus()
            return
        }

        if (tinggi == null || tinggi <= 0.0) {
            binding.etTinggi.error = "Tinggi badan tidak valid"
            binding.etTinggi.requestFocus()
            return
        }

        val statusgizi = viewmodel.hitungStatusGizi(berat, umur)

        binding.tvHasil.text =
            "Status Gizi: $statusgizi\nTanggal Pemeriksaan: ${formatTanggal.format(Date(tanggalPeriksaMillis))}"

        viewmodel.tambahData(
            namaIbu = namaIbu,
            namaAnak = nama,
            umur = umur,
            berat = berat,
            tinggi = tinggi,
            status = statusgizi,
            tanggalPeriksa = tanggalPeriksaMillis
        )

        Toast.makeText(
            requireContext(),
            "Data pemeriksaan berhasil disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}