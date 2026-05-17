package com.example.sibunda.ui.agenda

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgendaFragment : Fragment(R.layout.fragment_agenda) {

    private lateinit var calendarView: CalendarView
    private lateinit var txtTanggal: TextView
    private lateinit var txtBulan: TextView
    private lateinit var txtTahun: TextView
    private lateinit var txtInfo: TextView

    private val dataAgenda = mapOf(
        "2026-05-17" to "Hari Pemeriksaan Balita",
        "2026-05-20" to "Jadwal Imunisasi Campak",
        "2026-06-05" to "Pemberian Vitamin A"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendarView)
        txtTanggal = view.findViewById(R.id.txtTanggal)
        txtBulan = view.findViewById(R.id.txtBulan)
        txtTahun = view.findViewById(R.id.txtTahun)
        txtInfo = view.findViewById(R.id.txtInfo)

        val calendar = Calendar.getInstance()
        updateTanggalDanInfo(calendar)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            updateTanggalDanInfo(selectedCalendar)
        }
    }

    private fun updateTanggalDanInfo(calendar: Calendar) {
        val tanggal = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        val bulan = SimpleDateFormat("MMMM", Locale("id", "ID")).format(calendar.time)
        val tahun = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)

        txtTanggal.text = tanggal
        txtBulan.text = bulan
        txtTahun.text = tahun

        val keyTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        val agendaHariIni = dataAgenda[keyTanggal]

        if (agendaHariIni != null) {
            txtInfo.text = "$tanggal $bulan:\n$agendaHariIni"
        } else {
            txtInfo.text = "$tanggal $bulan:\nTidak ada agenda kegiatan"
        }
    }
}