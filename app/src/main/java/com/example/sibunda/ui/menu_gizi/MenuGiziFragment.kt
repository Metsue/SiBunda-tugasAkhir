package com.example.sibunda.ui.menu_gizi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentMenuGiziBinding

class MenuGiziFragment : Fragment() {

    private var _binding: FragmentMenuGiziBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuGiziBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daftarUmur = arrayOf(
            "Pilih Umur Balita",
            "0 - 6 Bulan",
            "7 - 12 Bulan",
            "13 - 24 Bulan",
            "25 - 36 Bulan",
            "37 - 60 Bulan"
        )

        val context = requireContext()
        val warnaTextPink = ContextCompat.getColor(context, R.color.text_dark_pink)
        val warnaTextGray = ContextCompat.getColor(context, R.color.text_gray)
        val warnaSoftPink = ContextCompat.getColor(context, R.color.soft_pink)

        val adapterSpinner = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            daftarUmur
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(warnaTextPink)
                view.textSize = 16f
                view.setPadding(20, 0, 20, 0)
                view.setBackgroundColor(warnaSoftPink)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(warnaTextPink)
                view.textSize = 16f
                view.setPadding(20, 20, 20, 20)
                view.setBackgroundColor(warnaSoftPink)
                return view
            }
        }

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUmur.adapter = adapterSpinner

        binding.btnCari.setOnClickListener {
            val umurDipilih = binding.spinnerUmur.selectedItem.toString()

            if (umurDipilih == "Pilih Umur Balita") {
                Toast.makeText(
                    context,
                    "Silakan pilih umur balita terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tampilkanPembahasan(umurDipilih, warnaTextGray)
            }
        }
    }

    private fun tampilkanPembahasan(umur: String, warnaText: Int) {
        val pembahasan = when (umur) {
            "0 - 6 Bulan" -> listOf(
                "ASI eksklusif sangat dianjurkan sampai usia 6 bulan.",
                "Belum perlu diberikan makanan pendamping ASI.",
                "Pantau berat badan bayi secara rutin setiap bulan.",
                "Pastikan bayi menyusu dengan cukup dan teratur."
            )
            "7 - 12 Bulan" -> listOf(
                "Mulai berikan MPASI dengan tekstur halus.",
                "Berikan makanan seperti bubur, buah lembut, dan sayur yang dihaluskan.",
                "Tetap lanjutkan pemberian ASI.",
                "Perhatikan kebersihan makanan dan alat makan bayi."
            )
            "13 - 24 Bulan" -> listOf(
                "Berikan makanan keluarga dengan tekstur yang lebih lembut.",
                "Pastikan makanan mengandung karbohidrat, protein, sayur, and buah.",
                "Berikan makanan utama 3 kali sehari dan selingan sehat.",
                "Pantau pertumbuhan tinggi dan berat badan."
            )
            "25 - 36 Bulan" -> listOf(
                "Biasakan anak makan teratur 3 kali sehari.",
                "Kurangi makanan manis dan jajanan tidak sehat.",
                "Berikan protein seperti telur, ikan, ayam, tahu, dan tempe.",
                "Ajak anak aktif bergerak untuk mendukung pertumbuhan."
            )
            "37 - 60 Bulan" -> listOf(
                "Berikan makanan bergizi seimbang setiap hari.",
                "Latih anak untuk makan sayur dan buah.",
                "Pastikan anak cukup minum air putih.",
                "Pantau status gizi agar tidak mengalami gizi kurang atau berlebih."
            )
            else -> listOf("Data pembahasan belum tersedia.")
        }

        val context = requireContext()
        val warnaContainer = ContextCompat.getColor(context, R.color.bg_container_pink)

        val adapterList = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            pembahasan
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(warnaText)
                view.textSize = 15f
                view.setPadding(20, 18, 20, 18)
                view.setBackgroundColor(warnaContainer)
                return view
            }
        }

        binding.lvPembahasan.adapter = adapterList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
