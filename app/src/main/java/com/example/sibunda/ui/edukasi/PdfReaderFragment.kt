package com.example.sibunda.ui.edukasi

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentPdfReaderBinding
import java.io.File
import java.io.FileOutputStream

class PdfReaderFragment : Fragment(R.layout.fragment_pdf_reader) {

    private var _binding: FragmentPdfReaderBinding? = null
    private val binding get() = _binding!!

    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    private var currentPageIndex = 0
    private var totalPages = 0

    private var fileAsset: String = ""
    private var judulEbook: String = ""
    private var deskripsiEbook: String = ""

    private var daftarBab: List<BabItem> = emptyList()

    data class BabItem(
        val judul: String,
        val halaman: Int
    ) {
        override fun toString(): String {
            return judul
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPdfReaderBinding.bind(view)

        ambilArgument()
        tampilkanHeader()
        setupButton()
        bukaPdfDariAssets()
    }

    private fun ambilArgument() {
        judulEbook = arguments?.getString("judul_ebook") ?: "Ebook Edukasi"
        deskripsiEbook = arguments?.getString("deskripsi_ebook") ?: "Buku panduan edukasi"
        fileAsset = arguments?.getString("file_asset") ?: ""
    }

    private fun tampilkanHeader() {
        binding.tvJudulReader.text = judulEbook
        binding.tvDeskripsiReader.text = deskripsiEbook
        binding.tvPageInfo.text = "0 / 0"
        binding.btnPrevPage.isEnabled = false
        binding.btnNextPage.isEnabled = false
    }

    private fun setupButton() {
        binding.btnBackReader.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPrevPage.setOnClickListener {
            if (currentPageIndex > 0) {
                currentPageIndex--
                tampilkanHalaman(currentPageIndex)
            }
        }

        binding.btnNextPage.setOnClickListener {
            if (currentPageIndex < totalPages - 1) {
                currentPageIndex++
                tampilkanHalaman(currentPageIndex)
            }
        }

        binding.btnKeHalaman.setOnClickListener {
            lompatKeHalamanInput()
        }
    }

    private fun setupDaftarBab() {
        daftarBab = getDaftarBabBerdasarkanFile(fileAsset)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            daftarBab
        )

        binding.actBabReader.setAdapter(adapter)

        binding.actBabReader.setOnItemClickListener { _, _, position, _ ->
            val bab = daftarBab[position]
            lompatKeHalaman(bab.halaman)
        }
    }

    private fun getDaftarBabBerdasarkanFile(asset: String): List<BabItem> {
        return when {
            asset.contains("ssgi_2024", ignoreCase = true) -> {
                listOf(
                    BabItem("Cover", 1),
                    BabItem("Daftar Isi", 4),
                    BabItem("BAB 1 - Pendahuluan", 27),
                    BabItem("BAB 2 - Metodologi", 30),
                    BabItem("BAB 3 - Cakupan dan Karakteristik", 40),
                    BabItem("BAB 4 - Status Gizi Balita 2024", 45),
                    BabItem("BAB 5 - Faktor Determinan Status Gizi", 105),
                    BabItem("Lampiran", 360)
                )
            }

            asset.contains("gizi_buruk_balita", ignoreCase = true) -> {
                listOf(
                    BabItem("Cover", 1),
                    BabItem("Daftar Isi", 7),
                    BabItem("BAB 1 - Pendahuluan", 11),
                    BabItem("BAB 2 - Pengelolaan Gizi Buruk Terintegrasi", 19),
                    BabItem("BAB 3 - Pencegahan dan Penemuan Dini", 27),
                    BabItem("BAB 4 - Tatalaksana Gizi Buruk", 41),
                    BabItem("BAB 5 - Pengelolaan Upaya Penanggulangan", 75),
                    BabItem("BAB 6 - Penutup", 87),
                    BabItem("Daftar Pustaka", 89)
                )
            }

            asset.contains("pemantauan_pertumbuhan", ignoreCase = true) -> {
                listOf(
                    BabItem("Cover", 1),
                    BabItem("Daftar Isi", 4),
                    BabItem("Definisi Operasional", 9),
                    BabItem("BAB 1 - Pendahuluan", 11),
                    BabItem("BAB 2 - Pelaksanaan Pemantauan Pertumbuhan", 17),
                    BabItem("BAB 3 - Sarana dan Prasarana", 21),
                    BabItem("BAB 4 - Penilaian Status Pertumbuhan di Posyandu", 45),
                    BabItem("BAB 5 - Penilaian Status Pertumbuhan di Fasyankes", 54),
                    BabItem("BAB 6 - Pencatatan dan Pelaporan", 60),
                    BabItem("BAB 7 - Monitoring dan Evaluasi", 63)
                )
            }

            asset.contains("buku_saku_pmt", ignoreCase = true) -> {
                listOf(
                    BabItem("Cover", 1),
                    BabItem("Kata Pengantar", 5),
                    BabItem("Daftar Isi", 7),
                    BabItem("Penggunaan Buku", 9),
                    BabItem("Mengenal Balita Bermasalah Gizi", 10),
                    BabItem("PMT Lokal bagi Balita Bermasalah Gizi", 25),
                    BabItem("Kegiatan Kader dalam Pelaksanaan PMT Lokal", 37),
                    BabItem("Contoh Menu dan Siklus Menu PMT Lokal", 77),
                    BabItem("Edukasi oleh Kader", 82),
                    BabItem("Cara Kader Berkomunikasi", 90),
                    BabItem("Pertanyaan dan Jawaban", 97)
                )
            }

            else -> {
                listOf(
                    BabItem("Cover", 1),
                    BabItem("Halaman 5", 5),
                    BabItem("Halaman 10", 10),
                    BabItem("Halaman 20", 20),
                    BabItem("Halaman 50", 50)
                )
            }
        }
    }

    private fun lompatKeHalamanInput() {
        val input = binding.etHalamanTujuan.text.toString().trim()

        if (input.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Masukkan nomor halaman terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val halaman = input.toIntOrNull()

        if (halaman == null) {
            Toast.makeText(
                requireContext(),
                "Nomor halaman tidak valid",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lompatKeHalaman(halaman)
    }

    private fun lompatKeHalaman(halaman: Int) {
        if (totalPages <= 0) {
            Toast.makeText(
                requireContext(),
                "PDF belum siap dibuka",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val halamanAman = halaman.coerceIn(1, totalPages)
        currentPageIndex = halamanAman - 1
        tampilkanHalaman(currentPageIndex)

        binding.etHalamanTujuan.setText(halamanAman.toString())
    }

    private fun bukaPdfDariAssets() {
        if (fileAsset.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "File ebook tidak ditemukan",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val file = copyAssetPdfKeCache(fileAsset)

            parcelFileDescriptor = ParcelFileDescriptor.open(
                file,
                ParcelFileDescriptor.MODE_READ_ONLY
            )

            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
            totalPages = pdfRenderer?.pageCount ?: 0

            if (totalPages > 0) {
                setupDaftarBab()
                currentPageIndex = 0
                tampilkanHalaman(currentPageIndex)
            } else {
                Toast.makeText(
                    requireContext(),
                    "PDF kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Gagal membuka ebook. Pastikan file PDF ada di folder assets/ebooks",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun copyAssetPdfKeCache(assetPath: String): File {
        val fileName = assetPath.substringAfterLast("/")
        val file = File(requireContext().cacheDir, fileName)

        requireContext().assets.open(assetPath).use { input ->
            FileOutputStream(file, false).use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun tampilkanHalaman(index: Int) {
        try {
            currentPage?.close()
            currentPage = null

            val renderer = pdfRenderer ?: return
            val page = renderer.openPage(index)
            currentPage = page

            val maxWidth = 1600

            val scale = if (page.width > 0) {
                maxWidth.toFloat() / page.width.toFloat()
            } else {
                1f
            }

            val bitmapWidth = (page.width * scale).toInt().coerceAtLeast(1)
            val bitmapHeight = (page.height * scale).toInt().coerceAtLeast(1)

            val bitmap = Bitmap.createBitmap(
                bitmapWidth,
                bitmapHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            page.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )

            binding.imgPdfPage.setImageBitmap(bitmap)
            binding.tvPageInfo.text = "${index + 1} / $totalPages"
            binding.etHalamanTujuan.setText((index + 1).toString())

            binding.btnPrevPage.isEnabled = index > 0
            binding.btnNextPage.isEnabled = index < totalPages - 1

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Gagal menampilkan halaman PDF",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        currentPage?.close()
        currentPage = null

        pdfRenderer?.close()
        pdfRenderer = null

        parcelFileDescriptor?.close()
        parcelFileDescriptor = null

        _binding = null
        super.onDestroyView()
    }
}