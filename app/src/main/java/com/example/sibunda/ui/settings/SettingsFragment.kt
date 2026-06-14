package com.example.sibunda.ui.settings

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.core.utils.ThemeManager
import com.example.sibunda.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        tampilkanDataUser()
        setupMenuListeners()
        terapkanTema()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            tampilkanDataUser()
            terapkanTema()
        }
    }

    private fun tampilkanDataUser() {
        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val nama = sharedPref.getString(Constants.KEY_NAMA, "Bunda") ?: "Bunda"
        val email = sharedPref.getString(Constants.KEY_USERNAME, "Belum login") ?: "Belum login"
        val foto = sharedPref.getString(Constants.KEY_FOTO_PROFILE, "") ?: ""

        binding.tvSettingsName.text = nama
        binding.tvSettingsEmail.text = email

        tampilkanFotoProfil(foto)
    }

    private fun tampilkanFotoProfil(foto: String) {
        if (foto.isNotEmpty()) {
            try {
                val uri = Uri.parse(foto)

                binding.ivProfilePicture.setPadding(0, 0, 0, 0)
                binding.ivProfilePicture.imageTintList = null
                binding.ivProfilePicture.clearColorFilter()
                binding.ivProfilePicture.setImageURI(uri)

            } catch (e: Exception) {
                tampilkanFotoDefault()
            }
        } else {
            tampilkanFotoDefault()
        }
    }

    private fun tampilkanFotoDefault() {
        binding.ivProfilePicture.setPadding(18, 18, 18, 18)
        binding.ivProfilePicture.imageTintList = null
        binding.ivProfilePicture.clearColorFilter()
        binding.ivProfilePicture.setImageResource(R.drawable.ic_profile)
    }

    private fun setupMenuListeners() {
        binding.btnMenuProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        binding.btnMenuTheme.setOnClickListener {
            tampilkanDialogTema()
        }

        binding.btnMenuNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationSettingsFragment)
        }

        binding.btnMenuAbout.setOnClickListener {
            findNavController().navigate(R.id.aboutSibundaFragment)
        }

        binding.btnLogout.setOnClickListener {
            logoutAkun()
        }
    }

    private fun tampilkanDialogTema() {
        val daftarTema = arrayOf(
            "Original / Cerah",
            "Tema Gelap",
            "Biru Eksklusif",
            "Hijau Eksklusif"
        )

        val valueTema = arrayOf(
            ThemeManager.THEME_ORIGINAL,
            ThemeManager.THEME_DARK,
            ThemeManager.THEME_BLUE,
            ThemeManager.THEME_GREEN
        )

        val temaSekarang = ThemeManager.getCurrentTheme(requireContext())
        val checkedItem = valueTema.indexOf(temaSekarang).takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Tema Warna")
            .setSingleChoiceItems(daftarTema, checkedItem) { dialog, which ->
                val temaDipilih = valueTema[which]
                val namaTemaDipilih = daftarTema[which]

                ThemeManager.saveTheme(requireContext(), temaDipilih)

                dialog.dismiss()

                Toast.makeText(
                    requireContext(),
                    "Tema diubah ke $namaTemaDipilih",
                    Toast.LENGTH_SHORT
                ).show()

                requireActivity().recreate()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun terapkanTema() {
        val colors = ThemeManager.getColors(requireContext())
        val namaTemaAktif = ThemeManager.getThemeName(
            ThemeManager.getCurrentTheme(requireContext())
        )

        requireActivity().window.statusBarColor = colors.background
        requireActivity().window.navigationBarColor = colors.navBackground

        binding.rootSettings.setBackgroundColor(colors.background)

        binding.tvHeaderSettings.setTextColor(colors.primary)
        binding.tvSettingsName.setTextColor(colors.textPrimary)
        binding.tvSettingsEmail.setTextColor(colors.textSecondary)

        binding.tvCurrentTheme.text = "Tema aktif: $namaTemaAktif"
        binding.tvCurrentTheme.setTextColor(colors.textSecondary)

        binding.cardUserSettings.setCardBackgroundColor(colors.card)
        binding.cardUserSettings.strokeColor = colors.divider
        binding.cardUserSettings.strokeWidth = 1

        binding.cardMenuSettings.setCardBackgroundColor(colors.card)
        binding.cardMenuSettings.strokeColor = colors.divider
        binding.cardMenuSettings.strokeWidth = 1

        binding.cardPhotoSettings.setCardBackgroundColor(colors.primarySoft)
        binding.cardPhotoSettings.strokeColor = colors.primary
        binding.cardPhotoSettings.strokeWidth = 1

        binding.tvMenuProfile.setTextColor(colors.textPrimary)
        binding.tvMenuTheme.setTextColor(colors.textPrimary)
        binding.tvMenuNotification.setTextColor(colors.textPrimary)
        binding.tvMenuAbout.setTextColor(colors.textPrimary)

        binding.tvArrowProfile.setTextColor(colors.textSecondary)
        binding.tvArrowTheme.setTextColor(colors.textSecondary)
        binding.tvArrowNotification.setTextColor(colors.textSecondary)
        binding.tvArrowAbout.setTextColor(colors.textSecondary)

        binding.lineProfile.setBackgroundColor(colors.divider)
        binding.lineTheme.setBackgroundColor(colors.divider)
        binding.lineNotification.setBackgroundColor(colors.divider)

        binding.btnLogout.backgroundTintList = ColorStateList.valueOf(colors.primary)
        binding.btnLogout.setTextColor(colors.buttonText)
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
            remove(Constants.KEY_FOTO_PROFILE)
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