package com.example.sibunda.ui.kelola_pertumbuhan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.databinding.FragmentKelolaPertumbuhanBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class KelolaPertumbuhanFragment : Fragment() {
    private var _binding: FragmentKelolaPertumbuhanBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: BalitaViewModel by viewModels()
    private lateinit var adapter: BalitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelolaPertumbuhanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuprecyclerview()

        viewmodel.searchResults.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data)
            updategrafik(data)
        }

        binding.edtSearch.addTextChangedListener { text ->
            viewmodel.setSearchQuery(text.toString())
        }
    }

    private fun setuprecyclerview() {
        adapter = BalitaAdapter()
        binding.rvpertumbuhan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvpertumbuhan.adapter = adapter
    }

    private fun updategrafik(list: List<Balita>) {
        if (list.isEmpty()) return

        val sortedlist = list.sortedBy { it.tanggal }
        val entryberat = ArrayList<Entry>()
        val entrytinggi = ArrayList<Entry>()

        sortedlist.forEachIndexed { index, balita ->
            entryberat.add(Entry(index.toFloat(), balita.berat.toFloat()))
            entrytinggi.add(Entry(index.toFloat(), balita.tinggi.toFloat()))
        }

        val datasetberat = LineDataSet(entryberat, "Berat Badan (kg)")
        val datasettedtinggi = LineDataSet(entrytinggi, "Tinggi Badan (cm)")

        val linedata = LineData(datasetberat, datasettedtinggi)
        binding.chartperkembangan.data = linedata
        binding.chartperkembangan.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
