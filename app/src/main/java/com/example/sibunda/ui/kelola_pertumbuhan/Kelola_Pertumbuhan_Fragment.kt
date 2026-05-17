package com.example.sibunda.ui.kelola_pertumbuhan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sibunda.R

class KelolaPertumbuhanFragment : Fragment() {

    private lateinit var btnback: ImageView
    private lateinit var edtsearch: EditText
    private lateinit var btncari: Button

    private lateinit var txtnama: TextView
    private lateinit var txtgender: TextView
    private lateinit var txtumur: TextView
    private lateinit var txtstatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kelola__pertumbuhan_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnback = view.findViewById(R.id.btnBack)
        edtsearch = view.findViewById(R.id.edtSearch)
        btncari = view.findViewById(R.id.btnCari)

        txtnama = view.findViewById(R.id.txtNama)
        txtgender = view.findViewById(R.id.txtGender)
        txtumur = view.findViewById(R.id.txtUmur)
        txtstatus = view.findViewById(R.id.txtStatus)

        btncari.setOnClickListener {
            val nama = edtsearch.text.toString()
            txtnama.text = "Nama : $nama"
            txtgender.text = "Jenis Kelamin : Laki-laki"
            txtumur.text = "Umur : 3 Tahun"
            txtstatus.text = "Status Gizi : Normal"
        }

        btnback.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}