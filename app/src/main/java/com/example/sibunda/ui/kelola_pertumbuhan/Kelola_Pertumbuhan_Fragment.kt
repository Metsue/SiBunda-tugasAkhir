package com.example.sibunda.ui.kelola_pertumbuhan

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sibunda.core.data.local.entity.Pertumbuhan
import com.example.sibunda.core.utils.Constants
import com.example.sibunda.databinding.FragmentKelolaPertumbuhanBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KelolaPertumbuhanFragment : Fragment() {

    private var _binding: FragmentKelolaPertumbuhanBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: BalitaViewModel by viewModels()

    private lateinit var adapter: BalitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelolaPertumbuhanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val namaIbu = sharedPref.getString("KEY_USERNAME", "Bunda") ?: "Bunda"

        setupRecyclerView()

        // Menampilkan list anak milik ibu
        viewmodel.getBalitaByMother(namaIbu)
            .observe(viewLifecycleOwner) { data ->
                adapter.submitList(data)
                // otomatis pilih anak pertama jika belum ada yang terpilih
                if (data.isNotEmpty()) {
                    viewmodel.selectBalita(data[0].id)
                }
            }

        // update grafik realtime
        viewmodel.riwayatPertumbuhan.observe(viewLifecycleOwner) { riwayat ->
            updateGrafik(riwayat)
        }

        // pencarian
        binding.edtSearch.addTextChangedListener { text ->
            viewmodel.setSearchQuery(text.toString())
        }

        // tombol pdf
        binding.btnDownloadPdf.setOnClickListener {
            downloadPdf()
        }

        // tombol back
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = BalitaAdapter { balita ->
            // klik item => update grafik
            viewmodel.selectBalita(balita.id)
        }
        binding.rvpertumbuhan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvpertumbuhan.adapter = adapter
    }

    private fun updateGrafik(riwayat: List<Pertumbuhan>) {
        if (riwayat.isEmpty()) {
            binding.chartperkembangan.clear()
            return
        }

        val entryBerat = ArrayList<Entry>()
        val entryTinggi = ArrayList<Entry>()

        riwayat.forEach { item ->
            entryBerat.add(Entry(item.umur.toFloat(), item.berat.toFloat()))
            entryTinggi.add(Entry(item.umur.toFloat(), item.tinggi.toFloat()))
        }

        val datasetBerat = LineDataSet(entryBerat, "Berat Badan (kg)").apply {
            color = resources.getColor(android.R.color.holo_red_light, null)
            setCircleColor(resources.getColor(android.R.color.holo_red_light, null))
            lineWidth = 3f
            circleRadius = 5f
        }

        val datasetTinggi = LineDataSet(entryTinggi, "Tinggi Badan (cm)").apply {
            color = resources.getColor(android.R.color.holo_blue_light, null)
            setCircleColor(resources.getColor(android.R.color.holo_blue_light, null))
            lineWidth = 3f
            circleRadius = 5f
        }

        val lineData = LineData(datasetBerat, datasetTinggi)
        binding.chartperkembangan.data = lineData
        binding.chartperkembangan.description.text = "Grafik Pertumbuhan Balita"
        binding.chartperkembangan.animateX(1000)
        binding.chartperkembangan.invalidate()
    }

    private fun downloadPdf() {
        lifecycleScope.launch {
            try {
                val riwayat = viewmodel.riwayatPertumbuhan.value ?: emptyList()
                if (riwayat.isEmpty()) {
                    Toast.makeText(requireContext(), "Data pertumbuhan kosong", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())
                if (!folder.exists()) {
                    folder.mkdirs()
                }

                val fileName = "Riwayat_Pertumbuhan_${riwayat[0].nama.replace(" ", "_")}.pdf"
                val file = File(folder, fileName)
                val writer = PdfWriter(file)
                val pdfDocument = PdfDocument(writer)
                val document = Document(pdfDocument)

                document.add(Paragraph("RIWAYAT PERTUMBUHAN BALITA").setBold().setFontSize(20f))
                document.add(Paragraph("Nama Balita: ${riwayat[0].nama}"))
                document.add(Paragraph(" "))

                val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))

                riwayat.forEachIndexed { index, item ->
                    val dateString = dateFormat.format(Date(item.tanggal))
                    document.add(Paragraph("""
                        Data Ke-${index + 1}
                        Umur           : ${item.umur} bulan
                        Berat Badan    : ${item.berat} kg
                        Tinggi Badan   : ${item.tinggi} cm
                        Status Gizi    : ${item.statusgizi}
                        Tanggal Periksa: $dateString
                        ------------------------------------
                    """.trimIndent()))
                }

                document.close()
                Toast.makeText(requireContext(), "PDF berhasil disimpan: $fileName", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal membuat PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
