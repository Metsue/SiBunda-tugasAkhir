package com.example.sibunda.ui.edukasi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentEbookBinding

class EbookFragment : Fragment(R.layout.fragment_ebook) {

    private var _binding: FragmentEbookBinding? = null
    private val binding get() = _binding!!

    data class EbookItem(
        val judul: String,
        val deskripsi: String,
        val fileAsset: String
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEbookBinding.bind(view)

        val ebook1 = EbookItem(
            judul = "SSGI 2024 Dalam Angka",
            deskripsi = "Data dan informasi status gizi balita di Indonesia.",
            fileAsset = "ebooks/ssgi_2024.pdf"
        )

        val ebook2 = EbookItem(
            judul = "Pedoman Gizi Buruk Balita",
            deskripsi = "Panduan pencegahan dan tatalaksana gizi buruk pada balita.",
            fileAsset = "ebooks/gizi_buruk_balita.pdf"
        )

        val ebook3 = EbookItem(
            judul = "Pedoman Pemantauan Pertumbuhan",
            deskripsi = "Panduan pemantauan pertumbuhan balita secara rutin.",
            fileAsset = "ebooks/pemantauan_pertumbuhan.pdf"
        )

        val ebook4 = EbookItem(
            judul = "Buku Saku PMT Lokal Balita",
            deskripsi = "Panduan PMT lokal bagi balita bermasalah gizi.",
            fileAsset = "ebooks/buku_saku_pmt.pdf"
        )

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cardEbook1.setOnClickListener {
            bukaReader(ebook1)
        }

        binding.cardEbook2.setOnClickListener {
            bukaReader(ebook2)
        }

        binding.cardEbook3.setOnClickListener {
            bukaReader(ebook3)
        }

        binding.cardEbook4.setOnClickListener {
            bukaReader(ebook4)
        }
    }

    private fun bukaReader(item: EbookItem) {
        val bundle = Bundle().apply {
            putString("judul_ebook", item.judul)
            putString("deskripsi_ebook", item.deskripsi)
            putString("file_asset", item.fileAsset)
        }

        findNavController().navigate(R.id.pdfReaderFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}