package com.example.sibunda.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class AgendaDummy(
    val tanggal: String, // format: yyyy-MM-dd
    val judul: String,
    val lokasi: String,
    val keterangan: String
)

object AgendaDataDummy {

    val listAgenda = listOf(
        AgendaDummy(
            tanggal = "2026-05-20",
            judul = "Imunisasi Rutin Posyandu",
            lokasi = "Balai Desa Banjarmasin",
            keterangan = "Pemeriksaan dan imunisasi rutin untuk balita."
        ),
        AgendaDummy(
            tanggal = "2026-06-05",
            judul = "Pemberian Vitamin A",
            lokasi = "Posyandu Melati",
            keterangan = "Pemberian vitamin A untuk balita usia 6–59 bulan."
        ),
        AgendaDummy(
            tanggal = "2026-06-10",
            judul = "Pemeriksaan Berat dan Tinggi Badan",
            lokasi = "Posyandu Mawar",
            keterangan = "Pengukuran berat badan dan tinggi badan balita."
        ),
        AgendaDummy(
            tanggal = "2026-06-17",
            judul = "Penyuluhan Gizi Balita",
            lokasi = "Balai Kelurahan",
            keterangan = "Edukasi kepada orang tua tentang gizi seimbang."
        ),
        AgendaDummy(
            tanggal = "2026-06-25",
            judul = "Konsultasi Gizi Anak",
            lokasi = "Puskesmas Terdekat",
            keterangan = "Konsultasi dengan petugas gizi mengenai tumbuh kembang balita."
        )
    )

    fun getAgendaByTanggal(tanggal: String): AgendaDummy? {
        return listAgenda.find { it.tanggal == tanggal }
    }

    fun getAgendaTerdekat(): AgendaDummy? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hariIni = Calendar.getInstance().time

        return listAgenda
            .mapNotNull { agenda ->
                try {
                    val tanggalAgenda = dateFormat.parse(agenda.tanggal)
                    if (tanggalAgenda != null && !tanggalAgenda.before(hariIni)) {
                        agenda
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    null
                }
            }
            .minByOrNull { dateFormat.parse(it.tanggal)?.time ?: Long.MAX_VALUE }
            ?: listAgenda.firstOrNull()
    }

    fun formatTanggalIndonesia(tanggal: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val date = input.parse(tanggal)
            if (date != null) output.format(date) else tanggal
        } catch (e: Exception) {
            tanggal
        }
    }
}