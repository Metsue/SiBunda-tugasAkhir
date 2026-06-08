package com.example.sibunda.ui.kontak_dokter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentKontakDokterBinding

class KontakDokterFragment : Fragment(R.layout.fragment_kontak_dokter) {

    private var _binding: FragmentKontakDokterBinding? = null
    private val binding get() = _binding!!

    private var jenisKonsultasi: String = ""
    private var deskripsiKonsultasi: String = ""
    private var namaDokter: String = ""
    private var nomorWhatsapp: String = ""
    private var jamLayanan: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKontakDokterBinding.bind(view)

        ambilDataBundle()
        tampilkanData()
        setupWhatsappButton()
    }

    private fun ambilDataBundle() {
        jenisKonsultasi = arguments?.getString("jenis_konsultasi").orEmpty()
        deskripsiKonsultasi = arguments?.getString("deskripsi_konsultasi").orEmpty()
        namaDokter = arguments?.getString("nama_dokter").orEmpty()
        nomorWhatsapp = arguments?.getString("nomor_whatsapp").orEmpty()
        jamLayanan = arguments?.getString("jam_layanan").orEmpty()
    }

    private fun tampilkanData() {
        binding.tvJenisKonsultasi.text = jenisKonsultasi.ifEmpty { "Konsultasi Gizi Balita" }
        binding.tvDeskripsiKonsultasi.text =
            deskripsiKonsultasi.ifEmpty { "Informasi konsultasi belum tersedia." }

        binding.tvNamaDokter.text = namaDokter.ifEmpty { "dr. Expransa Saputra" }

        binding.tvNomorWhatsapp.text = if (nomorWhatsapp.isNotEmpty()) {
            formatNomorWhatsapp(nomorWhatsapp)
        } else {
            "-"
        }

        binding.tvJamLayanan.text = jamLayanan.ifEmpty { "-" }
    }

    private fun setupWhatsappButton() {
        binding.btnWhatsapp.setOnClickListener {
            if (nomorWhatsapp.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Nomor WhatsApp tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val pesan = """
                Assalamu'alaikum dokter.
                
                Saya ingin melakukan $jenisKonsultasi.
                Mohon izin untuk berkonsultasi terkait kondisi anak saya.
                
                Terima kasih.
            """.trimIndent()

            val url = "https://wa.me/$nomorWhatsapp?text=${Uri.encode(pesan)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "WhatsApp tidak ditemukan di perangkat ini",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun formatNomorWhatsapp(nomor: String): String {
        return if (nomor.startsWith("62")) {
            "+$nomor"
        } else {
            nomor
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}