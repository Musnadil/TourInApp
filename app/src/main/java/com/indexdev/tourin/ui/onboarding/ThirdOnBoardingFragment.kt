package com.indexdev.tourin.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentThirdOnBoardingBinding
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.ON_BOARDING
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.SHARED_PREF
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdOnBoardingFragment : Fragment() {
    private var _binding: FragmentThirdOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = requireContext().getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        val onBoardingEditor = preference.edit()
        binding.btnNext.setOnClickListener {
            onBoardingEditor.putBoolean(ON_BOARDING,false)
            onBoardingEditor.apply()
            findNavController().navigate(R.id.action_thirdOnBoardingFragment_to_homeFragment2)
        }
    }
}