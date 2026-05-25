package com.example.sibunda.ui.edukasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sibunda.databinding.FragmentVideoEdukasiBinding

class VideoEdukasiFragment : Fragment() {

    private var _binding: FragmentVideoEdukasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoEdukasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cardVideo1.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=ysz5S6PUM-U")
        }

        binding.cardVideo2.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=jNQXAC9IVRw")
        }

        binding.cardVideo3.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=oHg5SJYRHA0")
        }

        binding.cardVideo4.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        }

        binding.cardVideo5.setOnClickListener {
            bukavideo("https://www.youtube.com/watch?v=3JZ_D3ELwOQ")
        }
    }

    private fun bukavideo(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
