package com.example.sibunda.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupNavigation()
    }

    private fun setupNavigation() {
        // Navigasi ke Cek Gizi
        binding.cardGizi.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_gizi)
        }

        // Navigasi ke Monitoring Pertumbuhan (MPAndroidChart)
        binding.cardMonitoring.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_monitoring)
        }

        // Navigasi ke Konsultasi (Chat)
        binding.cardKonsultasi.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_konsultasi)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}