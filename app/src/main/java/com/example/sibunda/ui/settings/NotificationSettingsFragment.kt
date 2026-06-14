package com.example.sibunda.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.core.utils.NotificationHelper
import com.example.sibunda.core.utils.ThemeManager
import com.example.sibunda.databinding.FragmentNotificationSettingsBinding
import java.util.Locale

class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {

    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    private var selectedHour = 7
    private var selectedMinute = 0

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(requireContext(), "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show()
            } else {
                binding.switchNotification.isChecked = false
                Toast.makeText(requireContext(), "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationSettingsBinding.bind(view)

        NotificationHelper.createNotificationChannel(requireContext())

        loadSettings()
        setupListeners()
        applyTheme()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            applyTheme()
        }
    }

    private fun loadSettings() {
        val pref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val enabled = pref.getBoolean(Constants.KEY_NOTIF_ENABLED, false)
        val dayBefore = pref.getBoolean(Constants.KEY_NOTIF_DAY_BEFORE, false)

        selectedHour = pref.getInt(Constants.KEY_NOTIF_HOUR, 7)
        selectedMinute = pref.getInt(Constants.KEY_NOTIF_MINUTE, 0)

        binding.switchNotification.isChecked = enabled
        binding.switchDayBefore.isChecked = dayBefore

        updateTimeText()
        updateStatusText()
    }

    private fun setupListeners() {
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestPermissionIfNeeded()
            }
            updateStatusText()
        }

        binding.btnTimePicker.setOnClickListener {
            tampilkanTimePicker()
        }

        binding.btnTestNotification.setOnClickListener {
            requestPermissionIfNeeded()

            NotificationHelper.showAgendaNotification(
                context = requireContext(),
                title = "Tes Notifikasi SiBunda",
                message = "Notifikasi agenda posyandu berhasil aktif. Ibu akan mendapat pengingat sesuai jadwal.",
                notificationId = 777
            )
        }

        binding.btnSaveNotification.setOnClickListener {
            simpanPengaturan()
        }
    }

    private fun tampilkanTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                updateTimeText()
            },
            selectedHour,
            selectedMinute,
            true
        )

        dialog.show()
    }

    private fun updateTimeText() {
        val time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
        binding.btnTimePicker.text = "Jam $time"
    }

    private fun updateStatusText() {
        binding.tvNotifStatusDesc.text = if (binding.switchNotification.isChecked) {
            "Notifikasi aktif. Aplikasi akan mengingatkan agenda posyandu sesuai waktu yang dipilih."
        } else {
            "Notifikasi belum aktif. Aktifkan agar agenda posyandu tidak terlewat."
        }
    }

    private fun simpanPengaturan() {
        val pref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        pref.edit()
            .putBoolean(Constants.KEY_NOTIF_ENABLED, binding.switchNotification.isChecked)
            .putBoolean(Constants.KEY_NOTIF_DAY_BEFORE, binding.switchDayBefore.isChecked)
            .putInt(Constants.KEY_NOTIF_HOUR, selectedHour)
            .putInt(Constants.KEY_NOTIF_MINUTE, selectedMinute)
            .apply()

        if (binding.switchNotification.isChecked) {
            requestPermissionIfNeeded()
            NotificationHelper.scheduleAgendaNotifications(requireContext())
        } else {
            NotificationHelper.cancelAgendaNotifications(requireContext())
        }

        Toast.makeText(
            requireContext(),
            "Pengaturan notifikasi berhasil disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun requestPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun applyTheme() {
        val colors = ThemeManager.getColors(requireContext())

        binding.rootNotification.setBackgroundColor(colors.background)

        binding.tvTitleNotification.setTextColor(colors.primary)
        binding.tvSubtitleNotification.setTextColor(colors.textSecondary)
        binding.tvNotifStatusTitle.setTextColor(colors.textPrimary)
        binding.tvNotifStatusDesc.setTextColor(colors.textSecondary)
        binding.tvLabelReminder.setTextColor(colors.textPrimary)
        binding.tvNotifExplain.setTextColor(colors.textSecondary)

        binding.cardNotifInfo.setCardBackgroundColor(colors.card)
        binding.cardNotifInfo.strokeColor = colors.divider
        binding.cardNotifInfo.strokeWidth = 1

        binding.cardNotifSetting.setCardBackgroundColor(colors.card)
        binding.cardNotifSetting.strokeColor = colors.divider
        binding.cardNotifSetting.strokeWidth = 1

        binding.lineNotif1.setBackgroundColor(colors.divider)
        binding.lineNotif2.setBackgroundColor(colors.divider)

        binding.switchNotification.setTextColor(colors.textPrimary)
        binding.switchDayBefore.setTextColor(colors.textPrimary)

        binding.btnTimePicker.backgroundTintList =
            android.content.res.ColorStateList.valueOf(colors.primary)
        binding.btnTestNotification.backgroundTintList =
            android.content.res.ColorStateList.valueOf(colors.primary)
        binding.btnSaveNotification.backgroundTintList =
            android.content.res.ColorStateList.valueOf(colors.primary)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}