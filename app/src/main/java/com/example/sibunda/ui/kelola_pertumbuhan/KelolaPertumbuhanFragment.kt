package com.example.sibunda.ui.kelola_pertumbuhan

import android.content.Context
import android.graphics.Color
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
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
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

    private val formatTanggal = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    private val formatFile = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    private var namaIbuAktif: String = "Bunda"

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

        namaIbuAktif = sharedPref.getString(Constants.KEY_NAMA, "Bunda") ?: "Bunda"

        setupRecyclerView()

        viewmodel.getBalitaByMother(namaIbuAktif)
            .observe(viewLifecycleOwner) { data ->
                adapter.submitList(data)
                if (data.isNotEmpty()) {
                    viewmodel.selectBalita(data[0].id)
                }
            }

        viewmodel.riwayatPertumbuhan.observe(viewLifecycleOwner) { riwayat ->
            updateGrafik(riwayat)
        }

        binding.edtSearch.addTextChangedListener { text ->
            viewmodel.setSearchQuery(text.toString())
        }

        binding.btnDownloadPdf.setOnClickListener {
            downloadPdfModern()
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = BalitaAdapter { balita ->
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

        val dataUrut = riwayat.sortedBy { it.tanggal }

        val entryBerat = ArrayList<Entry>()
        val entryTinggi = ArrayList<Entry>()

        dataUrut.forEachIndexed { index, item ->
            val xValue = (index + 1).toFloat()
            entryBerat.add(Entry(xValue, item.berat.toFloat()))
            entryTinggi.add(Entry(xValue, item.tinggi.toFloat()))
        }

        val datasetBerat = LineDataSet(entryBerat, "Berat Badan (kg)").apply {
            color = Color.rgb(244, 83, 111)
            setCircleColor(Color.rgb(244, 83, 111))
            valueTextColor = Color.rgb(47, 42, 53)
            lineWidth = 3f
            circleRadius = 5f
            setDrawValues(true)
        }

        val datasetTinggi = LineDataSet(entryTinggi, "Tinggi Badan (cm)").apply {
            color = Color.rgb(53, 169, 213)
            setCircleColor(Color.rgb(53, 169, 213))
            valueTextColor = Color.rgb(47, 42, 53)
            lineWidth = 3f
            circleRadius = 5f
            setDrawValues(true)
        }

        binding.chartperkembangan.data = LineData(datasetBerat, datasetTinggi)
        binding.chartperkembangan.description.text = "Grafik berdasarkan urutan tanggal pemeriksaan"
        binding.chartperkembangan.animateX(1000)
        binding.chartperkembangan.invalidate()
    }

    private fun downloadPdfModern() {
        lifecycleScope.launch {
            try {
                val riwayat = viewmodel.riwayatPertumbuhan.value
                    ?.sortedBy { it.tanggal }
                    ?: emptyList()

                if (riwayat.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Data pertumbuhan kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val folder = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ).toString()
                )

                if (!folder.exists()) {
                    folder.mkdirs()
                }

                val namaBalita = riwayat[0].nama
                val fileName =
                    "Laporan_Pertumbuhan_${namaBalita.replace(" ", "_")}_${formatFile.format(Date())}.pdf"

                val file = File(folder, fileName)

                val writer = PdfWriter(file)
                val pdfDocument = PdfDocument(writer)
                val document = Document(pdfDocument, PageSize.A4)
                document.setMargins(36f, 36f, 36f, 36f)

                val fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
                val fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA)

                val pink = DeviceRgb(244, 143, 177)
                val dark = DeviceRgb(47, 42, 53)
                val gray = DeviceRgb(100, 100, 100)
                val softPink = DeviceRgb(255, 230, 240)

                val title = Paragraph("SiBunda")
                    .setFont(fontBold)
                    .setFontSize(24f)
                    .setFontColor(pink)
                    .setTextAlignment(TextAlignment.CENTER)

                val subtitle = Paragraph("Laporan Riwayat Pertumbuhan dan Status Gizi Balita")
                    .setFont(fontBold)
                    .setFontSize(15f)
                    .setFontColor(dark)
                    .setTextAlignment(TextAlignment.CENTER)

                document.add(title)
                document.add(subtitle)

                val line = SolidLine(1f)
                line.color = pink
                document.add(LineSeparator(line).setMarginTop(10f).setMarginBottom(16f))

                val identitasTable = Table(UnitValue.createPercentArray(floatArrayOf(35f, 65f)))
                    .useAllAvailableWidth()
                    .setMarginBottom(18f)

                fun addIdentitas(label: String, value: String) {
                    identitasTable.addCell(
                        Cell()
                            .add(Paragraph(label).setFont(fontBold).setFontColor(dark))
                            .setBackgroundColor(softPink)
                            .setBorder(SolidBorder(pink, 0.5f))
                            .setPadding(8f)
                    )
                    identitasTable.addCell(
                        Cell()
                            .add(Paragraph(value).setFont(fontRegular).setFontColor(dark))
                            .setBorder(SolidBorder(pink, 0.5f))
                            .setPadding(8f)
                    )
                }

                addIdentitas("Nama Balita", namaBalita)
                addIdentitas("Nama Ibu", namaIbuAktif)
                addIdentitas("Tanggal Cetak", formatTanggal.format(Date()))
                addIdentitas("Jumlah Pemeriksaan", "${riwayat.size} kali")

                document.add(identitasTable)

                document.add(
                    Paragraph("Ringkasan Pemeriksaan Terakhir")
                        .setFont(fontBold)
                        .setFontSize(14f)
                        .setFontColor(dark)
                        .setMarginBottom(8f)
                )

                val terakhir = riwayat.last()

                val ringkasan = Paragraph(
                    "Pemeriksaan terakhir dilakukan pada ${formatTanggal.format(Date(terakhir.tanggal))}. " +
                            "Pada pemeriksaan tersebut, usia balita tercatat ${terakhir.umur} bulan, " +
                            "berat badan ${terakhir.berat} kg, tinggi badan ${terakhir.tinggi} cm, " +
                            "dengan status gizi ${terakhir.statusgizi}."
                )
                    .setFont(fontRegular)
                    .setFontSize(11f)
                    .setFontColor(gray)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setMarginBottom(16f)

                document.add(ringkasan)

                document.add(
                    Paragraph("Tabel Riwayat Pertumbuhan")
                        .setFont(fontBold)
                        .setFontSize(14f)
                        .setFontColor(dark)
                        .setMarginBottom(8f)
                )

                val table = Table(
                    UnitValue.createPercentArray(
                        floatArrayOf(8f, 24f, 14f, 18f, 18f, 18f)
                    )
                ).useAllAvailableWidth()

                val headers = listOf(
                    "No",
                    "Tanggal",
                    "Umur",
                    "Berat",
                    "Tinggi",
                    "Status"
                )

                headers.forEach { header ->
                    table.addHeaderCell(
                        Cell()
                            .add(
                                Paragraph(header)
                                    .setFont(fontBold)
                                    .setFontColor(DeviceRgb(255, 255, 255))
                                    .setFontSize(10f)
                            )
                            .setBackgroundColor(pink)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(7f)
                    )
                }

                riwayat.forEachIndexed { index, item ->
                    table.addCell(cellTable("${index + 1}", fontRegular))
                    table.addCell(cellTable(formatTanggal.format(Date(item.tanggal)), fontRegular))
                    table.addCell(cellTable("${item.umur} bln", fontRegular))
                    table.addCell(cellTable("${item.berat} kg", fontRegular))
                    table.addCell(cellTable("${item.tinggi} cm", fontRegular))
                    table.addCell(cellTable(item.statusgizi, fontRegular))
                }

                document.add(table)

                document.add(
                    Paragraph(
                        "\nCatatan: Laporan ini dibuat berdasarkan data pemeriksaan yang dimasukkan melalui aplikasi SiBunda. " +
                                "Untuk penilaian medis yang lebih akurat, pemeriksaan tetap perlu dikonsultasikan dengan tenaga kesehatan."
                    )
                        .setFont(fontRegular)
                        .setFontSize(10f)
                        .setFontColor(gray)
                        .setTextAlignment(TextAlignment.JUSTIFIED)
                )

                document.close()

                Toast.makeText(
                    requireContext(),
                    "PDF berhasil disimpan di Download: $fileName",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Gagal membuat PDF: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun cellTable(text: String, font: com.itextpdf.kernel.font.PdfFont): Cell {
        return Cell()
            .add(
                Paragraph(text)
                    .setFont(font)
                    .setFontSize(9.5f)
                    .setFontColor(DeviceRgb(47, 42, 53))
            )
            .setPadding(6f)
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(SolidBorder(DeviceRgb(230, 210, 220), 0.5f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
