package com.example.sibunda.ui.edukasi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentEdukasiBinding

class EdukasiFragment : Fragment(R.layout.fragment_edukasi) {

    private var _binding: FragmentEdukasiBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEdukasiBinding.bind(view)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnVideoEdu.setOnClickListener {
            findNavController().navigate(R.id.action_materi_to_video)
        }

        binding.btnEbookEdu.setOnClickListener {
            findNavController().navigate(R.id.action_materi_to_ebook)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
