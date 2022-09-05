package com.indexdev.tourin.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
        greeting()
    }
    @SuppressLint("SetTextI18n")
    private fun greeting(){
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {binding.tvGreating.text = "Good Afternoon,"}
            in 17..20 -> {binding.tvGreating.text = "Good Evening,"}
            in 21..23 -> {binding.tvGreating.text = "Good Night,"}
            else -> {binding.tvGreating.text = "Good Morning,"}
        }
    }
}