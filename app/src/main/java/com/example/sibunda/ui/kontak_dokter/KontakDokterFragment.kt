package com.example.sibunda.ui.kontak_dokter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sibunda.databinding.FragmentKontakDokterBinding

class KontakDokterFragment : Fragment() {

    private var _binding: FragmentKontakDokterBinding? = null
    private val binding get() = _binding!!

    private var jenis = ""
    private var dokter = ""
    private var nomorWa = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKontakDokterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming data is passed via arguments in a real scenario
        jenis = arguments?.getString("jenis") ?: "Konsultasi Gizi"
        dokter = arguments?.getString("dokter") ?: "Dr. Konsultan Gizi"
        nomorWa = arguments?.getString("nomor") ?: "6282173180602"

        binding.tvJenis.text = jenis
        binding.tvDokter.text = dokter
        binding.tvNomor.text = "+$nomorWa"

        binding.btnWhatsapp.setOnClickListener {
            bukaWhatsapp()
        }
    }

    private fun bukaWhatsapp() {
        val pesan = "Halo $dokter, saya ingin melakukan $jenis."
        val url = "https://wa.me/$nomorWa?text=${Uri.encode(pesan)}"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "WhatsApp belum terinstall",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
