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

        binding.btnLogin.setOnClickListener {
            loginAkun()
        }

        binding.tvToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun loginAkun() {
        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password harus diisi"
            binding.etPassword.requestFocus()
            return
        }

        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val savedEmail = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_USERNAME),
            null
        )

        val savedPassword = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_PASSWORD),
            null
        )

        if (savedEmail == null || savedPassword == null) {
            Toast.makeText(
                requireContext(),
                "Akun belum terdaftar, silakan daftar terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != savedPassword) {
            Toast.makeText(
                requireContext(),
                "Password salah",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val nama = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_NAMA),
            "Bunda"
        ) ?: "Bunda"

        val nik = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_NIK),
            "Belum diisi"
        ) ?: "Belum diisi"

        val telepon = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_TELEPON),
            "Belum diisi"
        ) ?: "Belum diisi"

        val alamat = sharedPref.getString(
            Constants.accountKey(email, Constants.KEY_ALAMAT),
            "Belum diisi"
        ) ?: "Belum diisi"

        with(sharedPref.edit()) {
            putString(Constants.KEY_NAMA, nama)
            putString(Constants.KEY_NIK, nik)
            putString(Constants.KEY_TELEPON, telepon)
            putString(Constants.KEY_ALAMAT, alamat)
            putString(Constants.KEY_USERNAME, email)
            putString(Constants.KEY_PASSWORD, password)
            putBoolean(Constants.KEY_IS_LOGIN, true)
            apply()
        }

        Toast.makeText(
            requireContext(),
            "Selamat datang, $nama",
            Toast.LENGTH_SHORT
        ).show()

        findNavController().navigate(R.id.action_login_to_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}