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
    private lateinit var allListTourAdapter: AllListTourAdapter
    private val listPopularTour: MutableList<ResponseTourList> = ArrayList()

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        const val ID_TOUR = "ID_TOUR"
        const val TOUR_NAME = "TOUR_NAME"
        const val IMG_URL = "IMG_URL"
        const val LAT = "LAT"
        const val LONG = "LONG"
        const val ADDRESS = "ADDRESS"
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
        fetchPopularTourList()
        fetchAllListTour()
        detailTour()

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

    private fun fetchPopularTourList() {
        homeViewModel.getPopularTourList()
        homeViewModel.popularTourList.observe(viewLifecycleOwner) { popularTourList ->
            when (popularTourList.status) {
                SUCCESS -> {
                    binding.shimmerPopularTour.visibility = View.GONE
                    if (!popularTourList.data.isNullOrEmpty()) {
                        listPopularTour.clear()
                        val sortedList =
                            popularTourList.data.sortedByDescending { data -> data.rating }
                        for (i in 0..4) {
                            listPopularTour.add(sortedList[i])
                        }
                    }
                    popularTourAdapter.submitData(listPopularTour)
                }
                ERROR -> {
                    Toast.makeText(requireContext(), popularTourList.message, Toast.LENGTH_SHORT)
                        .show()
                }
                LOADING -> {
                    binding.shimmerPopularTour.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchAllListTour() {
        homeViewModel.getAllTourList()
        homeViewModel.allListTour.observe(viewLifecycleOwner) { allTourList ->
            when (allTourList.status) {
                SUCCESS -> {
                    binding.shimmerAllTour.visibility = View.GONE
                    allListTourAdapter.submitData(allTourList.data)
                }
                ERROR -> {
                    Toast.makeText(requireContext(), allTourList.message, Toast.LENGTH_SHORT)
                        .show()
                }
                LOADING -> {
                    binding.shimmerAllTour.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun detailTour() {
        popularTourAdapter = PopularTourAdapter(object : PopularTourAdapter.OnClickListener {
            override fun onClickItem(data: ResponseTourList) {
                val POIBundle = Bundle()
                POIBundle.putString(ID_TOUR, data.idWisata)
                POIBundle.putString(TOUR_NAME, data.wisata)
                POIBundle.putString(LAT, data.lat)
                POIBundle.putString(LONG, data.longi)
                POIBundle.putString(ADDRESS, data.alamat)
                POIBundle.putString(IMG_URL, data.urlImage)
                findNavController().navigate(R.id.action_homeFragment_to_mapsFragment, POIBundle)
            }

        })
        binding.rvPopularTour.adapter = popularTourAdapter

        allListTourAdapter = AllListTourAdapter(object : AllListTourAdapter.OnclickListener {
            override fun onClickItem(data: ResponseTourList) {
                val POIBundle = Bundle()
                POIBundle.putString(ID_TOUR, data.idWisata)
                POIBundle.putString(TOUR_NAME, data.wisata)
                POIBundle.putString(LAT, data.lat)
                POIBundle.putString(LONG, data.longi)
                POIBundle.putString(ADDRESS, data.alamat)
                POIBundle.putString(IMG_URL, data.urlImage)
                findNavController().navigate(R.id.action_homeFragment_to_mapsFragment, POIBundle)
            }
        })
        binding.rvAllTour.adapter = allListTourAdapter
    }


}