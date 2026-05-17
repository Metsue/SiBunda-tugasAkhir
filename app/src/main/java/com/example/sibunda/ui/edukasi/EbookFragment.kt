package com.example.sibunda.ui.ebook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class EbookFragment : Fragment() {

    private lateinit var btnback: ImageView

    private lateinit var cardebook1: CardView
    private lateinit var cardebook2: CardView
    private lateinit var cardebook3: CardView
    private lateinit var cardebook4: CardView
    private lateinit var cardebook5: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idlayout = resources.getIdentifier("fragment_ebook", "layout", requireContext().packageName)
        return inflater.inflate(idlayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idpkg = requireContext().packageName
        val idback = resources.getIdentifier("btnBack", "id", idpkg)
        val idcard1 = resources.getIdentifier("cardEbook1", "id", idpkg)
        val idcard2 = resources.getIdentifier("cardEbook2", "id", idpkg)
        val idcard3 = resources.getIdentifier("cardEbook3", "id", idpkg)
        val idcard4 = resources.getIdentifier("cardEbook4", "id", idpkg)
        val idcard5 = resources.getIdentifier("cardEbook5", "id", idpkg)

        btnback = view.findViewById(idback)

        cardebook1 = view.findViewById(idcard1)
        cardebook2 = view.findViewById(idcard2)
        cardebook3 = view.findViewById(idcard3)
        cardebook4 = view.findViewById(idcard4)
        cardebook5 = view.findViewById(idcard5)

        btnback.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        cardebook1.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        cardebook2.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        cardebook3.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        cardebook4.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        cardebook5.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }
    }

    private fun bukapdf(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}