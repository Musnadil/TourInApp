package com.indexdev.tourin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.response.ResponseTourList
import com.indexdev.tourin.databinding.FragmentHomeBinding
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.SHARED_PREF
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.USERNAME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var popularTourAdapter: PopularTourAdapter

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val username = preference.getString(USERNAME, DEFAULT_VALUE)

        greeting(username ?: "Username")
        fetchTourList()
        detailTour()

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return

        }
    }

    @SuppressLint("SetTextI18n")
    private fun greeting(username: String) {

        binding.tvUsername.text = username
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {
                binding.tvGreating.text = "Good Afternoon,"
            }
            in 17..20 -> {
                binding.tvGreating.text = "Good Evening,"
            }
            in 21..23 -> {
                binding.tvGreating.text = "Good Night,"
            }
            else -> {
                binding.tvGreating.text = "Good Morning,"
            }
        }
    }
    private fun fetchTourList(){
        homeViewModel.getTourList()
        homeViewModel.tourList.observe(viewLifecycleOwner){ tourList ->
            when(tourList.status){
                SUCCESS -> {
//                    binding.shimmerPopularTour.visibility = View.GONE
                    popularTourAdapter.submitData(tourList.data)
                }
                ERROR -> {
                    Toast.makeText(requireContext(), tourList.message, Toast.LENGTH_SHORT)
                        .show()
//                    binding.shimmerPopularTour.visibility = View.GONE
                }
                LOADING ->{
//                    binding.shimmerPopularTour.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun detailTour(){
        popularTourAdapter = PopularTourAdapter(object : PopularTourAdapter.OnClickListener{
            override fun onClickItem(data: ResponseTourList) {
                TODO("Not yet implemented")
            }

        })
        binding.rvPopularTour.adapter = popularTourAdapter
    }


}