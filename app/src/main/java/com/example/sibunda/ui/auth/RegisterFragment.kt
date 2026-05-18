package com.example.sibunda.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && pass == confirmPass) {

                // --- AWAL TAMBAHAN SINKRONISASI DATA ---
                // Menyimpan data registrasi ke dalam SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("KEY_USERNAME", email) // Simpan email sebagai username
                    putString("KEY_NAMA", "Bunda Baru") // Nama default
                    putString("KEY_NIK", "Belum diisi")
                    putString("KEY_TELEPON", "Belum diisi")
                    putString("KEY_ALAMAT", "Belum diisi")
                    apply() // Eksekusi penyimpanan
                }
                // --- AKHIR TAMBAHAN SINKRONISASI DATA ---

                Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register_to_login)
            } else if (pass != confirmPass) {
                binding.etConfirmPassword.error = "Password tidak cocok"
            } else {
                Toast.makeText(context, "Silakan isi semua bidang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}