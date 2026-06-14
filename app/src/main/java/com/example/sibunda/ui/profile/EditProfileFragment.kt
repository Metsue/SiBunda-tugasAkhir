package com.example.sibunda.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var fotoProfileUri: String = ""

    private val pilihFotoLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                    fotoProfileUri = uri.toString()
                    binding.ivEditFotoProfile.setPadding(0, 0, 0, 0)
                    binding.ivEditFotoProfile.setImageURI(uri)
                    binding.ivEditFotoProfile.clearColorFilter()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil foto profil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        tampilkanDataLama()

        binding.btnPilihFoto.setOnClickListener {
            pilihFotoLauncher.launch(arrayOf("image/*"))
        }

        binding.ivEditFotoProfile.setOnClickListener {
            pilihFotoLauncher.launch(arrayOf("image/*"))
        }

        binding.btnSimpanProfil.setOnClickListener {
            simpanPerubahanProfil()
        }
    }

    private fun tampilkanDataLama() {
        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val nama = sharedPref.getString(Constants.KEY_NAMA, "") ?: ""
        val nik = sharedPref.getString(Constants.KEY_NIK, "") ?: ""
        val telepon = sharedPref.getString(Constants.KEY_TELEPON, "") ?: ""
        val alamat = sharedPref.getString(Constants.KEY_ALAMAT, "") ?: ""
        val email = sharedPref.getString(Constants.KEY_USERNAME, "") ?: ""
        fotoProfileUri = sharedPref.getString(Constants.KEY_FOTO_PROFILE, "") ?: ""

        binding.etEditNama.setText(nama)
        binding.etEditNik.setText(nik)
        binding.etEditTelepon.setText(telepon)
        binding.etEditAlamat.setText(alamat)
        binding.tvEmailAkun.text = "Email akun: $email"

        if (fotoProfileUri.isNotEmpty()) {
            binding.ivEditFotoProfile.setPadding(0, 0, 0, 0)
            binding.ivEditFotoProfile.setImageURI(Uri.parse(fotoProfileUri))
            binding.ivEditFotoProfile.clearColorFilter()
        }
    }

    private fun simpanPerubahanProfil() {
        val nama = binding.etEditNama.text.toString().trim()
        val nik = binding.etEditNik.text.toString().trim()
        val telepon = binding.etEditTelepon.text.toString().trim()
        val alamat = binding.etEditAlamat.text.toString().trim()

        when {
            nama.isEmpty() -> {
                binding.etEditNama.error = "Nama tidak boleh kosong"
                binding.etEditNama.requestFocus()
                return
            }

            nik.isEmpty() -> {
                binding.etEditNik.error = "NIK tidak boleh kosong"
                binding.etEditNik.requestFocus()
                return
            }

            nik.length != 16 -> {
                binding.etEditNik.error = "NIK harus 16 digit"
                binding.etEditNik.requestFocus()
                return
            }

            telepon.isEmpty() -> {
                binding.etEditTelepon.error = "Nomor telepon tidak boleh kosong"
                binding.etEditTelepon.requestFocus()
                return
            }

            alamat.isEmpty() -> {
                binding.etEditAlamat.error = "Alamat tidak boleh kosong"
                binding.etEditAlamat.requestFocus()
                return
            }
        }

        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val email = sharedPref.getString(Constants.KEY_USERNAME, "") ?: ""

        with(sharedPref.edit()) {
            putString(Constants.KEY_NAMA, nama)
            putString(Constants.KEY_NIK, nik)
            putString(Constants.KEY_TELEPON, telepon)
            putString(Constants.KEY_ALAMAT, alamat)
            putString(Constants.KEY_FOTO_PROFILE, fotoProfileUri)

            if (email.isNotEmpty()) {
                putString(Constants.accountKey(email, Constants.KEY_NAMA), nama)
                putString(Constants.accountKey(email, Constants.KEY_NIK), nik)
                putString(Constants.accountKey(email, Constants.KEY_TELEPON), telepon)
                putString(Constants.accountKey(email, Constants.KEY_ALAMAT), alamat)
                putString(Constants.accountKey(email, Constants.KEY_FOTO_PROFILE), fotoProfileUri)
            }

            apply()
        }

        Toast.makeText(
            requireContext(),
            "Profil berhasil diperbarui",
            Toast.LENGTH_SHORT
        ).show()

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}