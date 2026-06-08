package com.example.sibunda.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.AgendaDataDummy
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        tampilkanNamaUser()
        tampilkanAgendaDiDashboard()
        setupNavigation()
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            tampilkanNamaUser()
            tampilkanAgendaDiDashboard()
        }
    }

    private fun tampilkanNamaUser() {
        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val nama = sharedPref.getString(Constants.KEY_NAMA, "Bunda") ?: "Bunda"

        binding.tvSapaan.text = "Halo $nama"
    }

    private fun tampilkanAgendaDiDashboard() {
        val agenda = AgendaDataDummy.getAgendaTerdekat()

        if (agenda != null) {
            binding.tvTanggalAgendaHome.text =
                "Tanggal: ${AgendaDataDummy.formatTanggalIndonesia(agenda.tanggal)}"

            binding.tvJudulAgendaHome.text =
                agenda.judul.uppercase()

            binding.tvLokasiAgendaHome.text =
                "Lokasi: ${agenda.lokasi}"
        } else {
            binding.tvTanggalAgendaHome.text = "Tanggal: -"
            binding.tvJudulAgendaHome.text = "Belum ada agenda posyandu"
            binding.tvLokasiAgendaHome.text = "Lokasi: -"
        }

        binding.cardInfoAgenda.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_agenda)
        }
    }

    private fun setupNavigation() {
        binding.cardGizi.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_gizi)
        }

        binding.cardMonitoring.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_kelola)
        }

        binding.cardKonsultasi.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_konsultasi)
        }

        binding.cardMateriEdukasi.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_materi)
        }

        binding.cardMakan.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_makan)
        }

        binding.cardAgenda.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_agenda)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}