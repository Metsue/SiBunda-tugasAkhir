package com.example.sibunda.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.core.utils.ThemeManager
import com.example.sibunda.databinding.FragmentAboutSibundaBinding

class AboutSibundaFragment : Fragment(R.layout.fragment_about_sibunda) {

    private var _binding: FragmentAboutSibundaBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutSibundaBinding.bind(view)

        applyTheme()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            applyTheme()
        }
    }

    private fun applyTheme() {
        val colors = ThemeManager.getColors(requireContext())

        binding.rootAbout.setBackgroundColor(colors.background)

        binding.tvTitleAbout.setTextColor(colors.primary)
        binding.tvAppNameAbout.setTextColor(colors.textPrimary)
        binding.tvVersionAbout.setTextColor(colors.textSecondary)
        binding.tvDescAbout.setTextColor(colors.textSecondary)
        binding.tvFeatureTitle.setTextColor(colors.textPrimary)
        binding.tvFeatureList.setTextColor(colors.textSecondary)
        binding.tvNoteTitle.setTextColor(colors.textPrimary)
        binding.tvNoteDesc.setTextColor(colors.textSecondary)

        binding.cardAboutHeader.setCardBackgroundColor(colors.card)
        binding.cardAboutHeader.strokeColor = colors.divider
        binding.cardAboutHeader.strokeWidth = 1

        binding.cardAboutFeature.setCardBackgroundColor(colors.card)
        binding.cardAboutFeature.strokeColor = colors.divider
        binding.cardAboutFeature.strokeWidth = 1

        binding.cardAboutNote.setCardBackgroundColor(colors.card)
        binding.cardAboutNote.strokeColor = colors.divider
        binding.cardAboutNote.strokeWidth = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}