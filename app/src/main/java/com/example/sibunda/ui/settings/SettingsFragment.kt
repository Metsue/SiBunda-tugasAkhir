package com.example.sibunda.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        setupMenuListeners()
    }

    private fun setupMenuListeners() {
        // Aksi menu ubah profil
        binding.btnMenuProfile.setOnClickListener {
            Toast.makeText(context, "Fitur Ubah Profil segera hadir", Toast.LENGTH_SHORT).show()
        }

        // Aksi ganti tema warna placeholder
        binding.btnMenuTheme.setOnClickListener {
            Toast.makeText(context, "Pilihan Tema: Soft Pink (Aktif)", Toast.LENGTH_SHORT).show()
        }

        // Aksi pengaturan notifikasi
        binding.btnMenuNotification.setOnClickListener {
            Toast.makeText(context, "Pengaturan Notifikasi dibuka", Toast.LENGTH_SHORT).show()
        }

        // Aksi tentang aplikasi
        binding.btnMenuAbout.setOnClickListener {
            Toast.makeText(context, "SiBunda v1.0 - Monitoring Gizi Balita", Toast.LENGTH_SHORT).show()
        }

        // Aksi Logout Akun (Kembali ke halaman login screen)
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

            // Navigasi kembali ke loginFragment dan hapus stack history dashboard
            findNavController().navigate(R.id.action_global_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}