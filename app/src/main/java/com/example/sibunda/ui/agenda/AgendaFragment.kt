package com.example.sibunda.ui.agenda

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
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
        tampilkanRingkasanBulanIni()
        tampilkanAgendaTerdekat()

        binding.btnPrevMonth.setOnClickListener {
            calendarAktif.add(Calendar.MONTH, -1)
            tampilkanKalender()
            tampilkanRingkasanBulanIni()
            tampilkanAgendaTerdekat()
        }

        binding.btnNextMonth.setOnClickListener {
            calendarAktif.add(Calendar.MONTH, 1)
            tampilkanKalender()
            tampilkanRingkasanBulanIni()
            tampilkanAgendaTerdekat()
        }

        binding.btnBulanIni.setOnClickListener {
            calendarAktif.timeInMillis = System.currentTimeMillis()
            tanggalDipilih.timeInMillis = System.currentTimeMillis()
            tampilkanHeaderHariIni()
            tampilkanKalender()
            tampilkanRingkasanBulanIni()
            tampilkanAgendaTerdekat()
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
                textSize = 12f
                setTextColor(Color.DKGRAY)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 36
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

        hariPertama = if (hariPertama == Calendar.SUNDAY) {
            7
        } else {
            hariPertama - 1
        }

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
                height = 45
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
            text = tanggal.toString()
            gravity = Gravity.CENTER
            textSize = 12f
            setPadding(0, 2, 0, 2)

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
                height = 45
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(2, 2, 2, 2)
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
            """.trimIndent()
        } else {
            binding.txtInfo.text = """
                $tanggalIndo

                Tidak ada agenda kegiatan
            """.trimIndent()
        }
    }

    private fun tampilkanRingkasanBulanIni() {
        val agendaBulanIni = AgendaDataDummy.listAgenda.filter { agenda ->
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(agenda.tanggal)!!

            cal.get(Calendar.MONTH) == calendarAktif.get(Calendar.MONTH) &&
                    cal.get(Calendar.YEAR) == calendarAktif.get(Calendar.YEAR)
        }

        val bulan = SimpleDateFormat("MMMM yyyy", localeIndonesia).format(calendarAktif.time)

        binding.tvRingkasanAgenda.text =
            "Ada ${agendaBulanIni.size} agenda posyandu pada $bulan. Tanggal yang memiliki agenda diberi tanda berwarna pink."
    }

    private fun tampilkanAgendaTerdekat() {
        binding.containerAgendaTerdekat.removeAllViews()

        val agendaBulanIni = AgendaDataDummy.listAgenda.filter { agenda ->
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(agenda.tanggal)!!

            cal.get(Calendar.MONTH) == calendarAktif.get(Calendar.MONTH) &&
                    cal.get(Calendar.YEAR) == calendarAktif.get(Calendar.YEAR)
        }.sortedBy { it.tanggal }

        if (agendaBulanIni.isEmpty()) {
            val kosong = TextView(requireContext()).apply {
                text = "Belum ada agenda pada bulan ini."
                textSize = 14f
                setTextColor(Color.GRAY)
                setPadding(8, 12, 8, 12)
            }

            binding.containerAgendaTerdekat.addView(kosong)
            return
        }

        agendaBulanIni.forEach { agenda ->
            val card = buatCardAgenda(
                tanggal = AgendaDataDummy.formatTanggalIndonesia(agenda.tanggal),
                judul = agenda.judul,
                lokasi = agenda.lokasi,
                keterangan = agenda.keterangan
            )
            binding.containerAgendaTerdekat.addView(card)
        }
    }

    private fun buatCardAgenda(
        tanggal: String,
        judul: String,
        lokasi: String,
        keterangan: String
    ): View {
        val card = com.google.android.material.card.MaterialCardView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 12
            }

            radius = 22f
            cardElevation = 4f
            setCardBackgroundColor(Color.WHITE)
            strokeWidth = 1
            strokeColor = Color.parseColor("#F48FB1")
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val icon = TextView(requireContext()).apply {
            text = "📌"
            textSize = 24f
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_home_soft_circle)
            layoutParams = LinearLayout.LayoutParams(58, 58)
        }

        val textContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                leftMargin = 14
            }
        }

        val tvTanggal = TextView(requireContext()).apply {
            text = tanggal
            textSize = 12f
            setTextColor(Color.parseColor("#8A7F88"))
        }

        val tvJudul = TextView(requireContext()).apply {
            text = judul
            textSize = 15f
            setTextColor(Color.parseColor("#2F2A35"))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val tvLokasi = TextView(requireContext()).apply {
            text = "Lokasi: $lokasi"
            textSize = 13f
            setTextColor(Color.parseColor("#6E6472"))
        }

        val tvKet = TextView(requireContext()).apply {
            text = keterangan
            textSize = 12f
            setTextColor(Color.parseColor("#6E6472"))
            setPadding(0, 4, 0, 0)
        }

        textContainer.addView(tvTanggal)
        textContainer.addView(tvJudul)
        textContainer.addView(tvLokasi)
        textContainer.addView(tvKet)

        container.addView(icon)
        container.addView(textContainer)
        card.addView(container)

        return card
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