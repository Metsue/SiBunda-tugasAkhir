package com.example.sibunda.ui.konsultasi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentKonsultasiBinding

class KonsultasiFragment : Fragment(R.layout.fragment_konsultasi) {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!

    data class KonsultasiItem(
        val jenis: String,
        val deskripsi: String,
        val namaDokter: String,
        val nomorWhatsapp: String,
        val jamLayanan: String
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKonsultasiBinding.bind(view)

        val konsultasiGizi = KonsultasiItem(
            jenis = "Konsultasi Gizi Balita",
            deskripsi = "Layanan konsultasi untuk membahas status gizi balita, kebutuhan nutrisi harian, serta evaluasi hasil pemeriksaan gizi.",
            namaDokter = "dr. Expransa Saputra",
            nomorWhatsapp = "6281549110563",
            jamLayanan = "Senin - Jumat, 08.00 - 16.00 WIB"
        )

        val konsultasiStunting = KonsultasiItem(
            jenis = "Konsultasi Stunting",
            deskripsi = "Layanan konsultasi untuk memantau pertumbuhan anak, mengenali risiko stunting, dan memperoleh saran tindak lanjut.",
            namaDokter = "dr. Nabila Rahmawati",
            nomorWhatsapp = "6281234567890",
            jamLayanan = "Senin - Jumat, 09.00 - 15.00 WIB"
        )

        val konsultasiPolaMakan = KonsultasiItem(
            jenis = "Konsultasi Pola Makan",
            deskripsi = "Layanan konsultasi untuk pengaturan jadwal makan, variasi menu, serta pola pemberian makanan yang sesuai usia balita.",
            namaDokter = "dr. Siti Azzahra Putri",
            nomorWhatsapp = "6289876543210",
            jamLayanan = "Senin - Sabtu, 08.30 - 14.30 WIB"
        )

        binding.cardKonsultasiGiziBalita.setOnClickListener {
            bukaKontakDokter(konsultasiGizi)
        }

        binding.cardKonsultasiStunting.setOnClickListener {
            bukaKontakDokter(konsultasiStunting)
        }

        binding.cardKonsultasiPolaMakan.setOnClickListener {
            bukaKontakDokter(konsultasiPolaMakan)
        }
    }

    private fun bukaKontakDokter(item: KonsultasiItem) {
        val bundle = Bundle().apply {
            putString("jenis_konsultasi", item.jenis)
            putString("deskripsi_konsultasi", item.deskripsi)
            putString("nama_dokter", item.namaDokter)
            putString("nomor_whatsapp", item.nomorWhatsapp)
            putString("jam_layanan", item.jamLayanan)
        }

        findNavController().navigate(R.id.kontakDokterFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}