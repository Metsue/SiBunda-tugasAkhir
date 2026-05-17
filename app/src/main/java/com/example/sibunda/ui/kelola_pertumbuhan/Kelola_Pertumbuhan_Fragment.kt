package com.example.sibunda.ui.pertumbuhan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

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
        val idlayout = resources.getIdentifier("fragment_kelola__pertumbuhan_", "layout", requireContext().packageName)
        return inflater.inflate(idlayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idpkg = requireContext().packageName
        val idback = resources.getIdentifier("btnBack", "id", idpkg)
        val idsearch = resources.getIdentifier("edtSearch", "id", idpkg)
        val idcari = resources.getIdentifier("btnCari", "id", idpkg)
        val idnama = resources.getIdentifier("txtNama", "id", idpkg)
        val idgender = resources.getIdentifier("txtGender", "id", idpkg)
        val idumur = resources.getIdentifier("txtUmur", "id", idpkg)
        val idstatus = resources.getIdentifier("txtStatus", "id", idpkg)

        btnback = view.findViewById(idback)
        edtsearch = view.findViewById(idsearch)
        btncari = view.findViewById(idcari)

        txtnama = view.findViewById(idnama)
        txtgender = view.findViewById(idgender)
        txtumur = view.findViewById(idumur)
        txtstatus = view.findViewById(idstatus)

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