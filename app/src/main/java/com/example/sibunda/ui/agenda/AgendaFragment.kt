package com.example.sibunda.ui.agenda

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.core.utils.AgendaDataDummy
import com.example.sibunda.databinding.FragmentAgendaBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgendaFragment : Fragment(R.layout.fragment_agenda) {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    private val calendarAktif: Calendar = Calendar.getInstance()
    private val tanggalDipilih: Calendar = Calendar.getInstance()

    private val localeIndonesia = Locale("id", "ID")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAgendaBinding.bind(view)

        tampilkanHeaderHariIni()
        buatHeaderHari()
        tampilkanKalender()

        binding.btnPrevMonth.setOnClickListener {
            calendarAktif.add(Calendar.MONTH, -1)
            tampilkanKalender()
        }

        binding.btnNextMonth.setOnClickListener {
            calendarAktif.add(Calendar.MONTH, 1)
            tampilkanKalender()
        }

        binding.btnBulanIni.setOnClickListener {
            calendarAktif.timeInMillis = System.currentTimeMillis()
            tanggalDipilih.timeInMillis = System.currentTimeMillis()
            tampilkanHeaderHariIni()
            tampilkanKalender()
        }
    }

    private fun tampilkanHeaderHariIni() {
        val tanggal = SimpleDateFormat("dd", Locale.getDefault()).format(tanggalDipilih.time)
        val bulan = SimpleDateFormat("MMMM", localeIndonesia).format(tanggalDipilih.time)
        val tahun = SimpleDateFormat("yyyy", Locale.getDefault()).format(tanggalDipilih.time)

        binding.txtTanggal.text = tanggal
        binding.txtBulan.text = bulan
        binding.txtTahun.text = tahun

        updateInfoAgenda(tanggalDipilih)
    }

    private fun buatHeaderHari() {
        binding.gridHari.removeAllViews()

        val namaHari = listOf("M", "S", "S", "R", "K", "J", "S")

        namaHari.forEach { hari ->
            val textView = TextView(requireContext()).apply {
                text = hari
                gravity = Gravity.CENTER
                textSize = 13f
                setTextColor(Color.DKGRAY)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 42
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }

            binding.gridHari.addView(textView)
        }
    }

    private fun tampilkanKalender() {
        binding.gridKalender.removeAllViews()

        val namaBulan = SimpleDateFormat("MMMM yyyy", localeIndonesia).format(calendarAktif.time)
        binding.txtNamaBulan.text = namaBulan

        val calendarBulan = calendarAktif.clone() as Calendar
        calendarBulan.set(Calendar.DAY_OF_MONTH, 1)

        val jumlahHariBulanIni = calendarBulan.getActualMaximum(Calendar.DAY_OF_MONTH)

        var hariPertama = calendarBulan.get(Calendar.DAY_OF_WEEK)

        // Ubah supaya Senin menjadi awal minggu
        hariPertama = if (hariPertama == Calendar.SUNDAY) {
            7
        } else {
            hariPertama - 1
        }

        // Kotak kosong sebelum tanggal 1
        for (i in 1 until hariPertama) {
            tambahKotakKosong()
        }

        for (tanggal in 1..jumlahHariBulanIni) {
            val calendarTanggal = calendarAktif.clone() as Calendar
            calendarTanggal.set(Calendar.DAY_OF_MONTH, tanggal)

            val tanggalKey = formatKeyTanggal(calendarTanggal)
            val adaAgenda = AgendaDataDummy.getAgendaByTanggal(tanggalKey) != null
            val isSelected = samaTanggal(calendarTanggal, tanggalDipilih)

            tambahTanggal(tanggal, adaAgenda, isSelected, calendarTanggal)
        }
    }

    private fun tambahKotakKosong() {
        val textView = TextView(requireContext()).apply {
            text = ""
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 58
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        }

        binding.gridKalender.addView(textView)
    }

    private fun tambahTanggal(
        tanggal: Int,
        adaAgenda: Boolean,
        isSelected: Boolean,
        calendarTanggal: Calendar
    ) {
        val textView = TextView(requireContext()).apply {
            text = if (adaAgenda) {
                "$tanggal\n•"
            } else {
                tanggal.toString()
            }

            gravity = Gravity.CENTER
            textSize = 13f
            setPadding(0, 4, 0, 4)

            if (isSelected) {
                setTextColor(Color.WHITE)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_tanggal_terpilih)
            } else if (adaAgenda) {
                setTextColor(Color.parseColor("#FF2D8D"))
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_tanggal_agenda)
            } else {
                setTextColor(Color.DKGRAY)
                background = null
            }

            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 58
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(3, 3, 3, 3)
            }

            setOnClickListener {
                tanggalDipilih.timeInMillis = calendarTanggal.timeInMillis
                tampilkanHeaderHariIni()
                tampilkanKalender()
            }
        }

        binding.gridKalender.addView(textView)
    }

    private fun updateInfoAgenda(calendar: Calendar) {
        val keyTanggal = formatKeyTanggal(calendar)
        val tanggalIndo = AgendaDataDummy.formatTanggalIndonesia(keyTanggal)
        val agenda = AgendaDataDummy.getAgendaByTanggal(keyTanggal)

        if (agenda != null) {
            binding.txtInfo.text = """
                $tanggalIndo

                ${agenda.judul}

                Lokasi:
                ${agenda.lokasi}

                ${agenda.keterangan}
            """.trimIndent()
        } else {
            binding.txtInfo.text = """
                $tanggalIndo

                Tidak ada agenda kegiatan
            """.trimIndent()
        }
    }

    private fun formatKeyTanggal(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun samaTanggal(a: Calendar, b: Calendar): Boolean {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
                a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}