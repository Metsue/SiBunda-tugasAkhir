package com.example.sibunda.ui.edukasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.sibunda.R

class EdukasiFragment : Fragment(R.layout.fragment_edukasi) {

    private lateinit var btnBack: ImageView
    private lateinit var btnVideoEdu: LinearLayout
    private lateinit var btnEbookEdu: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = view.findViewById(R.id.btnBack)
        btnVideoEdu = view.findViewById(R.id.btnVideoEdu)
        btnEbookEdu = view.findViewById(R.id.btnEbookEdu)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnVideoEdu.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/")
            )

            startActivity(intent)
        }

        btnEbookEdu.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/")
            )

            startActivity(intent)
        }
    }
}