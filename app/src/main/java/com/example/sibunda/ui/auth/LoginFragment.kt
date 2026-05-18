package com.example.sibunda.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        // Logika tombol masuk
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                // --- AWAL TAMBAHAN SINKRONISASI DATA ---
                // Memperbarui session login dengan email yang sedang aktif
                val sharedPref = requireActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("KEY_USERNAME", email)
                    apply()
                }
                // --- AKHIR TAMBAHAN SINKRONISASI DATA ---

                // Skenario Blackbox: Jika valid, pindah ke Home (Dashboard)
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(context, "Silakan isi semua bidang", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke halaman registrasi
        binding.tvToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}