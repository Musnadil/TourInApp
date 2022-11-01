package com.indexdev.tourin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentSplashScreenBinding
import com.indexdev.tourin.ui.initiateNotify
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    companion object {
        const val SHARED_PREF = "SHAREDPREF"
        const val ON_BOARDING = "ON_BOARDING"
        const val ID_USER = "ID_USER"
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
        val login = preference.getString(TOKEN, DEFAULT_VALUE)
        Handler(Looper.getMainLooper()).postDelayed({
            if (findNavController().currentDestination?.id == R.id.splashScreenFragment) {
                if (initiateNotify) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_ratingFragment2)
                } else if (login != DEFAULT_VALUE) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                } else {
                    if (showOnBoarding) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_firstOnBoardingFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }
                }
            }
        },3000)
    }
}