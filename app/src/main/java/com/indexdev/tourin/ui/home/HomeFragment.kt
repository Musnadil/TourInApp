package com.indexdev.tourin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.response.ResponseRecommendation
import com.indexdev.tourin.data.model.response.ResponseTourList
import com.indexdev.tourin.databinding.FragmentHomeBinding
import com.indexdev.tourin.ui.edit.EditAccountDialogFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment
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

        binding.cardUser.setOnClickListener {
            if (username == DEFAULT_VALUE) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                val dialogFragment = EditAccountDialogFragment(usernameUpdate = {
                    val preference =
                        requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                    val newUsername = preference.getString(USERNAME, DEFAULT_VALUE)
                    binding.tvUsername.text = newUsername
                })
                activity?.let { dialogFragment.show(it.supportFragmentManager, null) }
            }
        }

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
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        if (username == DEFAULT_VALUE){
            binding.tvGreating.visibility = View.GONE
            binding.tvUsername.text = "Masuk"
        } else{
            binding.tvUsername.text = username
            when (calendar.get(Calendar.HOUR_OF_DAY)) {
                in 12..14 -> {
                    binding.tvGreating.text = "Selamat siang,"
                }
                in 15..18 -> {
                    binding.tvGreating.text = "Selamat sore,"
                }
                in 19..23 -> {
                    binding.tvGreating.text = "Selamat malam,"
                }
                else -> {
                    binding.tvGreating.text = "Selamat pagi,"
                }
            }
        }

    }

    private fun fetchPopularTourList() {
        homeViewModel.getPopularTourList()
        homeViewModel.popularTourList.observe(viewLifecycleOwner) { popularTourList ->
            when (popularTourList.status) {
                SUCCESS -> {
                    binding.shimmerPopularTour.visibility = View.GONE
                    binding.rvPopularTour.visibility = View.VISIBLE
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
                    val snackbar = Snackbar.make(
                        binding.root, "Tidak dapat terhubung ke server",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("Oke") {
                        snackbar.dismiss()
                    }
                    snackbar.show()
                    binding.shimmerPopularTour.visibility = View.VISIBLE
                }
                LOADING -> {
                    binding.rvPopularTour.visibility = View.GONE
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
                    binding.rvAllTour.visibility = View.VISIBLE
                    binding.shimmerAllTour.visibility = View.GONE
                    allListTourAdapter.submitData(allTourList.data)
                }
                ERROR -> {
                    binding.shimmerAllTour.visibility = View.VISIBLE

                }
                LOADING -> {
                    binding.rvAllTour.visibility = View.GONE
                    binding.shimmerAllTour.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun detailTour() {
        val preference = requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val username = preference.getString(USERNAME, DEFAULT_VALUE)
        popularTourAdapter = PopularTourAdapter(object : PopularTourAdapter.OnClickListener {
            override fun onClickItem(data: ResponseTourList) {
                val POIBundle = Bundle()
                if (username == DEFAULT_VALUE){
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage("Untuk melihat wisata anda harus login terlebih dahulu.")
                        .setPositiveButton("Ok") { positiveButton, _ ->
                            positiveButton.dismiss()
                            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                        }
                        .setNegativeButton("Batal") { negativeButton, _ ->
                            negativeButton.dismiss()
                        }
                        .show()
                } else {
                    POIBundle.putString(ID_TOUR, data.idWisata)
                    POIBundle.putString(TOUR_NAME, data.wisata)
                    POIBundle.putString(LAT, data.lat)
                    POIBundle.putString(LONG, data.longi)
                    POIBundle.putString(ADDRESS, data.alamat)
                    POIBundle.putString(IMG_URL, data.urlImage)
                    findNavController().navigate(R.id.action_homeFragment_to_mapsFragment, POIBundle)
                }

            }

        })
        binding.rvPopularTour.adapter = popularTourAdapter

        allListTourAdapter = AllListTourAdapter(object : AllListTourAdapter.OnclickListener {
            override fun onClickItem(data: ResponseTourList) {
                val POIBundle = Bundle()
                if (username == DEFAULT_VALUE) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage("Untuk melihat wisata anda harus login terlebih dahulu.")
                        .setPositiveButton("Ok") { positiveButton, _ ->
                            positiveButton.dismiss()
                            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                        }
                        .setNegativeButton("Batal") { negativeButton, _ ->
                            negativeButton.dismiss()
                        }
                        .show()
                } else {
                    POIBundle.putString(ID_TOUR, data.idWisata)
                    POIBundle.putString(TOUR_NAME, data.wisata)
                    POIBundle.putString(LAT, data.lat)
                    POIBundle.putString(LONG, data.longi)
                    POIBundle.putString(ADDRESS, data.alamat)
                    POIBundle.putString(IMG_URL, data.urlImage)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_mapsFragment,
                        POIBundle
                    )
                }
            }
        })
        binding.rvAllTour.adapter = allListTourAdapter
    }

}