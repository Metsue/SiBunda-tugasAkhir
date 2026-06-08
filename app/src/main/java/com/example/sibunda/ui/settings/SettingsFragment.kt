package com.example.sibunda.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        tampilkanDataUser()
        setupMenuListeners()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            tampilkanDataUser()
        }
    }

    private fun tampilkanDataUser() {
        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val nama = sharedPref.getString(Constants.KEY_NAMA, "Bunda") ?: "Bunda"
        val email = sharedPref.getString(Constants.KEY_USERNAME, "Belum login") ?: "Belum login"

        binding.tvSettingsName.text = nama
        binding.tvSettingsEmail.text = email
    }

    private fun setupMenuListeners() {
        binding.btnMenuProfile.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Fitur Ubah Profil segera hadir",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnMenuTheme.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tema Soft Pink sedang aktif",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnMenuNotification.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Pengaturan notifikasi agenda segera hadir",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnMenuAbout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "SiBunda v1.0 - Aplikasi Monitoring Gizi Balita",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnLogout.setOnClickListener {
            logoutAkun()
        }
    }

    private fun logoutAkun() {
        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            remove(Constants.KEY_NAMA)
            remove(Constants.KEY_USERNAME)
            remove(Constants.KEY_PASSWORD)
            remove(Constants.KEY_NIK)
            remove(Constants.KEY_TELEPON)
            remove(Constants.KEY_ALAMAT)
            putBoolean(Constants.KEY_IS_LOGIN, false)
            apply()
        }

        Toast.makeText(
            requireContext(),
            "Berhasil keluar akun",
            Toast.LENGTH_SHORT
        ).show()

        findNavController().navigate(R.id.action_global_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}