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
            registerAkun()
        }
    }

    private fun registerAkun() {
        val nama = binding.etNama.text.toString().trim()
        val nik = binding.etNik.text.toString().trim()
        val telepon = binding.etTelepon.text.toString().trim()
        val alamat = binding.etAlamat.text.toString().trim()
        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        when {
            nama.isEmpty() -> {
                binding.etNama.error = "Nama lengkap harus diisi"
                binding.etNama.requestFocus()
                return
            }

            nik.isEmpty() -> {
                binding.etNik.error = "NIK harus diisi"
                binding.etNik.requestFocus()
                return
            }

            nik.length != 16 -> {
                binding.etNik.error = "NIK harus 16 digit"
                binding.etNik.requestFocus()
                return
            }

            telepon.isEmpty() -> {
                binding.etTelepon.error = "Nomor telepon harus diisi"
                binding.etTelepon.requestFocus()
                return
            }

            alamat.isEmpty() -> {
                binding.etAlamat.error = "Alamat rumah harus diisi"
                binding.etAlamat.requestFocus()
                return
            }

            email.isEmpty() -> {
                binding.etEmail.error = "Email harus diisi"
                binding.etEmail.requestFocus()
                return
            }

            !email.contains("@") || !email.contains(".") -> {
                binding.etEmail.error = "Format email tidak valid"
                binding.etEmail.requestFocus()
                return
            }

            password.isEmpty() -> {
                binding.etPassword.error = "Password harus diisi"
                binding.etPassword.requestFocus()
                return
            }

            password.length < 6 -> {
                binding.etPassword.error = "Password minimal 6 karakter"
                binding.etPassword.requestFocus()
                return
            }

            confirmPassword.isEmpty() -> {
                binding.etConfirmPassword.error = "Konfirmasi password harus diisi"
                binding.etConfirmPassword.requestFocus()
                return
            }

            password != confirmPassword -> {
                binding.etConfirmPassword.error = "Password tidak cocok"
                binding.etConfirmPassword.requestFocus()
                return
            }
        }

        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val akunSudahAda = sharedPref.contains(
            Constants.accountKey(email, Constants.KEY_USERNAME)
        )

        if (akunSudahAda) {
            Toast.makeText(
                requireContext(),
                "Email sudah terdaftar, silakan login",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        with(sharedPref.edit()) {
            putString(Constants.accountKey(email, Constants.KEY_NAMA), nama)
            putString(Constants.accountKey(email, Constants.KEY_NIK), nik)
            putString(Constants.accountKey(email, Constants.KEY_TELEPON), telepon)
            putString(Constants.accountKey(email, Constants.KEY_ALAMAT), alamat)
            putString(Constants.accountKey(email, Constants.KEY_USERNAME), email)
            putString(Constants.accountKey(email, Constants.KEY_PASSWORD), password)

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
            "Registrasi berhasil. Silakan login, $nama",
            Toast.LENGTH_SHORT
        ).show()

        findNavController().navigate(R.id.action_register_to_login)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}