package com.example.sibunda.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupObservers()

        // Memuat data profil saat fragment aktif
        viewModel.loadUserProfile()

        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Fitur edit profil dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.namaIbu.observe(viewLifecycleOwner) { nama ->
            binding.tvProfileName.text = nama
        }
        viewModel.nikIbu.observe(viewLifecycleOwner) { nik ->
            binding.tvProfileNik.text = nik
        }
        viewModel.usernameIbu.observe(viewLifecycleOwner) { username ->
            binding.tvProfileUsername.text = username
        }
        viewModel.teleponIbu.observe(viewLifecycleOwner) { telp ->
            binding.tvProfilePhone.text = telp
        }
        viewModel.alamatIbu.observe(viewLifecycleOwner) { alamat ->
            binding.tvProfileAddress.text = alamat
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}