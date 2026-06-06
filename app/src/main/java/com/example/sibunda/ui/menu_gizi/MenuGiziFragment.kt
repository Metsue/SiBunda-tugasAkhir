package com.example.sibunda.ui.menu_gizi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentMenuGiziBinding

class MenuGiziFragment : Fragment() {

    private var _binding: FragmentMenuGiziBinding? = null
    private val binding get() = _binding!!

    data class PembahasanGizi(
        val teks: String,
        val gambar: Int
    )

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

        setupSpinner()
        setupButton()
    }

    private fun setupSpinner() {
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
        val warnaSoftPink = ContextCompat.getColor(context, R.color.soft_pink)

        val adapterSpinner = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            daftarUmur
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val itemView = super.getView(position, convertView, parent) as TextView
                itemView.setTextColor(warnaTextPink)
                itemView.textSize = 16f
                itemView.setPadding(24, 0, 24, 0)
                itemView.setBackgroundColor(warnaSoftPink)
                return itemView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val itemView = super.getDropDownView(position, convertView, parent) as TextView
                itemView.setTextColor(warnaTextPink)
                itemView.textSize = 16f
                itemView.setPadding(24, 22, 24, 22)
                itemView.setBackgroundColor(warnaSoftPink)
                return itemView
            }
        }

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUmur.adapter = adapterSpinner
    }

    private fun setupButton() {
        binding.btnCari.setOnClickListener {
            val umurDipilih = binding.spinnerUmur.selectedItem.toString()

            if (umurDipilih == "Pilih Umur Balita") {
                Toast.makeText(
                    requireContext(),
                    "Silakan pilih umur balita terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tampilkanPembahasan(umurDipilih)
            }
        }
    }

    private fun tampilkanPembahasan(umur: String) {
        val pembahasan = when (umur) {
            "0 - 6 Bulan" -> listOf(
                PembahasanGizi(
                    "ASI eksklusif sangat dianjurkan sampai usia 6 bulan.",
                    R.drawable.img_asi
                ),
                PembahasanGizi(
                    "Belum perlu diberikan makanan pendamping ASI karena sistem pencernaan bayi masih berkembang.",
                    R.drawable.img_asi
                ),
                PembahasanGizi(
                    "Pantau berat badan bayi secara rutin setiap bulan agar pertumbuhannya tetap terkontrol.",
                    R.drawable.img_gizi_seimbang
                ),
                PembahasanGizi(
                    "Pastikan bayi menyusu dengan cukup dan teratur untuk memenuhi kebutuhan gizinya.",
                    R.drawable.img_asi
                )
            )

            "7 - 12 Bulan" -> listOf(
                PembahasanGizi(
                    "Mulai berikan MPASI dengan tekstur halus seperti bubur lembut atau puree.",
                    R.drawable.img_mpasi
                ),
                PembahasanGizi(
                    "Berikan makanan seperti bubur, buah lembut, dan sayur yang dihaluskan.",
                    R.drawable.img_mpasi
                ),
                PembahasanGizi(
                    "Tetap lanjutkan pemberian ASI karena ASI masih menjadi sumber gizi penting.",
                    R.drawable.img_asi
                ),
                PembahasanGizi(
                    "Perhatikan kebersihan makanan dan alat makan bayi agar terhindar dari gangguan pencernaan.",
                    R.drawable.img_mpasi
                )
            )

            "13 - 24 Bulan" -> listOf(
                PembahasanGizi(
                    "Berikan makanan keluarga dengan tekstur yang lebih lembut dan mudah dikunyah.",
                    R.drawable.img_makanan_keluarga
                ),
                PembahasanGizi(
                    "Pastikan makanan mengandung karbohidrat, protein, sayur, dan buah.",
                    R.drawable.img_gizi_seimbang
                ),
                PembahasanGizi(
                    "Berikan makanan utama 3 kali sehari dan selingan sehat di antara waktu makan.",
                    R.drawable.img_makan_teratur
                ),
                PembahasanGizi(
                    "Pantau pertumbuhan tinggi dan berat badan anak secara rutin.",
                    R.drawable.img_gizi_seimbang
                )
            )

            "25 - 36 Bulan" -> listOf(
                PembahasanGizi(
                    "Biasakan anak makan teratur 3 kali sehari dengan porsi yang sesuai.",
                    R.drawable.img_makan_teratur
                ),
                PembahasanGizi(
                    "Kurangi makanan manis dan jajanan tidak sehat agar anak terbiasa makan makanan bergizi.",
                    R.drawable.img_gizi_seimbang
                ),
                PembahasanGizi(
                    "Berikan protein seperti telur, ikan, ayam, tahu, dan tempe untuk mendukung pertumbuhan.",
                    R.drawable.img_makanan_keluarga
                ),
                PembahasanGizi(
                    "Ajak anak aktif bergerak untuk membantu perkembangan fisik dan motoriknya.",
                    R.drawable.img_makan_teratur
                )
            )

            "37 - 60 Bulan" -> listOf(
                PembahasanGizi(
                    "Berikan makanan bergizi seimbang setiap hari sesuai kebutuhan usia anak.",
                    R.drawable.img_gizi_seimbang
                ),
                PembahasanGizi(
                    "Latih anak untuk makan sayur dan buah agar terbiasa dengan makanan sehat.",
                    R.drawable.img_gizi_seimbang
                ),
                PembahasanGizi(
                    "Pastikan anak cukup minum air putih dan tidak terlalu banyak minuman manis.",
                    R.drawable.img_makan_teratur
                ),
                PembahasanGizi(
                    "Pantau status gizi anak agar tidak mengalami gizi kurang atau gizi berlebih.",
                    R.drawable.img_gizi_seimbang
                )
            )

            else -> listOf(
                PembahasanGizi(
                    "Data pembahasan belum tersedia.",
                    R.drawable.img_gizi_seimbang
                )
            )
        }

        tampilkanItemPembahasan(pembahasan)
    }

    private fun tampilkanItemPembahasan(dataPembahasan: List<PembahasanGizi>) {
        binding.containerPembahasan.removeAllViews()

        for (item in dataPembahasan) {
            val itemView = LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.item_pembahasan_gizi,
                    binding.containerPembahasan,
                    false
                )

            val imgPembahasan = itemView.findViewById<ImageView>(R.id.imgPembahasan)
            val tvPembahasan = itemView.findViewById<TextView>(R.id.tvPembahasan)

            imgPembahasan.setImageResource(item.gambar)
            tvPembahasan.text = item.teks

            binding.containerPembahasan.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}