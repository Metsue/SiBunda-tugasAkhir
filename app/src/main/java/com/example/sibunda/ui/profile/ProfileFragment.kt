package com.example.sibunda.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUserProfile()
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

        viewModel.teleponIbu.observe(viewLifecycleOwner) { telepon ->
            binding.tvProfilePhone.text = telepon
        }

        viewModel.alamatIbu.observe(viewLifecycleOwner) { alamat ->
            binding.tvProfileAddress.text = alamat
        }

        viewModel.fotoProfile.observe(viewLifecycleOwner) { foto ->
            if (!foto.isNullOrEmpty()) {
                binding.ivProfilePicture.setPadding(0, 0, 0, 0)
                binding.ivProfilePicture.setImageURI(Uri.parse(foto))
                binding.ivProfilePicture.clearColorFilter()
            } else {
                binding.ivProfilePicture.setImageResource(R.drawable.ic_profile)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}