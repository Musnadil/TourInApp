package com.indexdev.tourin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    companion object {
        const val SHARED_PREF = "SHAREDPREF"
        const val ON_BOARDING = "ON_BOARDING"
        const val ID_USER = "ID_USER"
        const val ID_TOUR = "ID_TOUR"
        const val IMG_URL = "IMG_URL"
        const val TOUR_NAME = "TOUR_NAME"
        const val USERNAME = "USERNAME"
        const val TOKEN = "TOKEN"
        const val DEFAULT_VALUE = "DEFAULT_VALUE"
    }

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = requireContext().getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        val showOnBoarding = preference.getBoolean(ON_BOARDING,true)
        val idTour = preference.getString(ID_TOUR, DEFAULT_VALUE)
        val login = preference.getString(TOKEN, DEFAULT_VALUE)

        Handler(Looper.getMainLooper()).postDelayed({
            if (findNavController().currentDestination?.id == R.id.splashScreenFragment) {
                if (showOnBoarding) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_firstOnBoardingFragment)
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                }
            }
        },3000)
    }
}