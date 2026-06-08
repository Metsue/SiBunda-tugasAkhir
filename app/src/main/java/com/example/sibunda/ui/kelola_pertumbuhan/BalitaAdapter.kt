package com.example.sibunda.ui.kelola_pertumbuhan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.databinding.ItemBalitaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BalitaAdapter(
    private val onItemClick: (Balita) -> Unit
) : ListAdapter<Balita, BalitaAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: ItemBalitaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val formatTanggal = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        fun bind(balita: Balita, onItemClick: (Balita) -> Unit) {
            binding.tvnamabalita.text = balita.nama
            binding.tvinfobalita.text =
                "${balita.umur} bulan | BB: ${balita.berat} kg | TB: ${balita.tinggi} cm\nPemeriksaan: ${formatTanggal.format(Date(balita.tanggal))}"
            binding.tvstatusgizi.text = balita.statusgizi

            binding.root.setOnClickListener {
                onItemClick(balita)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBalitaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Balita>() {
        override fun areItemsTheSame(olditem: Balita, newitem: Balita): Boolean {
            return olditem.id == newitem.id
        }

        override fun areContentsTheSame(olditem: Balita, newitem: Balita): Boolean {
            return olditem == newitem
        }
    }
}