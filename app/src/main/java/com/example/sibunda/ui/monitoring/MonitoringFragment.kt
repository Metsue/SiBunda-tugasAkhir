package com.example.sibunda.ui.monitoring

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentMonitoringBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MonitoringFragment : Fragment(R.layout.fragment_monitoring) {
    private var _binding: FragmentMonitoringBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMonitoringBinding.bind(view)

        displayGrowthChart()
    }

    private fun displayGrowthChart() {
        // Contoh data dummy pertumbuhan berat badan balita
        val entries = listOf(
            Entry(1f, 3.2f), // Bulan 1
            Entry(2f, 4.5f), // Bulan 2
            Entry(3f, 5.8f)  // Bulan 3
        )

        val dataSet = LineDataSet(entries, "Berat Badan (kg)").apply {
            color = resources.getColor(R.color.rose_pink, null)
            setCircleColor(resources.getColor(R.color.rose_pink, null))
            lineWidth = 3f
            mode = LineDataSet.Mode.CUBIC_BEZIER // Agar grafik melengkung halus
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate() // Refresh grafik
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}