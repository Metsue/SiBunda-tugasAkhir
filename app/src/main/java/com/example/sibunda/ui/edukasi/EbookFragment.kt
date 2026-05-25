package com.example.sibunda.ui.edukasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sibunda.databinding.FragmentEbookBinding

class EbookFragment : Fragment() {

    private var _binding: FragmentEbookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cardEbook1.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        binding.cardEbook2.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        binding.cardEbook3.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        binding.cardEbook4.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }

        binding.cardEbook5.setOnClickListener {
            bukapdf("https://drive.google.com/")
        }
    }

    private fun bukapdf(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
