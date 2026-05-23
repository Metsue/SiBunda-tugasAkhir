package com.example.sibunda.ui.kelola_pertumbuhan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sibunda.core.data.local.entity.Balita
import com.example.sibunda.databinding.ItemBalitaBinding

class BalitaAdapter : ListAdapter<Balita, BalitaAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemBalitaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(balita: Balita) {
            binding.tvnamabalita.text = balita.nama
            binding.tvinfobalita.text = "${balita.umur} bulan | BB: ${balita.berat} kg | TB: ${balita.tinggi} cm"
            binding.tvstatusgizi.text = balita.statusgizi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBalitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Balita>() {
        override fun areItemsTheSame(olditem: Balita, newitem: Balita): Boolean = olditem.id == newitem.id
        override fun areContentsTheSame(olditem: Balita, newitem: Balita): Boolean = olditem == newitem
    }
}