package com.example.sibunda.ui.konsultasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentKonsultasiBinding

class KonsultasiFragment : Fragment() {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKonsultasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGiziBalita.setOnClickListener {
            bukaKontakDokter(
                jenis = "Konsultasi Gizi Balita",
                dokter = "Dr. Expransa Saputra",
                nomor = "6281549110563"
            )
        }

        binding.btnStunting.setOnClickListener {
            bukaKontakDokter(
                jenis = "Konsultasi Stunting",
                dokter = "Dr. Muhammad Indra",
                nomor = "6283137970669"
            )
        }

        binding.btnPolaMakan.setOnClickListener {
            bukaKontakDokter(
                jenis = "Konsultasi Pola Makan",
                dokter = "Dr. Muhammad Rif'at Maulana",
                nomor = "6282173180602"
            )
        }
    }

    private fun bukaKontakDokter(jenis: String, dokter: String, nomor: String) {
        val bundle = Bundle().apply {
            putString("jenis", jenis)
            putString("dokter", dokter)
            putString("nomor", nomor)
        }
        // Use Navigation Component as defined in nav_graph.xml
        findNavController().navigate(R.id.action_konsultasi_to_kontak, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
