package com.example.sibunda.ui.edukasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class VideoEdukasiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idLayout = resources.getIdentifier("fragment_video_edukasi", "layout", requireContext().packageName)
        return inflater.inflate(idLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPkg = requireContext().packageName
        val idBack = resources.getIdentifier("btnBack", "id", idPkg)
        val idCard1 = resources.getIdentifier("cardVideo1", "id", idPkg)
        val idCard2 = resources.getIdentifier("cardVideo2", "id", idPkg)
        val idCard3 = resources.getIdentifier("cardVideo3", "id", idPkg)
        val idCard4 = resources.getIdentifier("cardVideo4", "id", idPkg)
        val idCard5 = resources.getIdentifier("cardVideo5", "id", idPkg)

        val btnback = view.findViewById<ImageView>(idBack)
        val cardvideo1 = view.findViewById<MaterialCardView>(idCard1)
        val cardvideo2 = view.findViewById<MaterialCardView>(idCard2)
        val cardvideo3 = view.findViewById<MaterialCardView>(idCard3)
        val cardvideo4 = view.findViewById<MaterialCardView>(idCard4)
        val cardvideo5 = view.findViewById<MaterialCardView>(idCard5)

        btnback.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        cardvideo1.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=ysz5S6PUM-U")
        }

        cardvideo2.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=jNQXAC9IVRw")
        }

        cardvideo3.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=oHg5SJYRHA0")
        }

        cardvideo4.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        }

        cardvideo5.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=3JZ_D3ELwOQ")
        }
    }

    private fun bukavideo(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}