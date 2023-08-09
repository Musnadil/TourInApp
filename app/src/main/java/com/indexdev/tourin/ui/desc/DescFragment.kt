package com.indexdev.tourin.ui.desc

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status
import com.indexdev.tourin.data.model.response.ResponsePOI
import com.indexdev.tourin.data.model.response.ResponseUserMitra
import com.indexdev.tourin.databinding.FragmentDescBinding
import com.indexdev.tourin.ui.home.HomeFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.SHARED_PREF
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.USERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescFragment : Fragment() {
    private var _binding: FragmentDescBinding? = null
    private val binding get() = _binding!!
    private val mapsViewModel: DescViewModel by viewModels()
    var listFacility: MutableList<ResponsePOI> = ArrayList()
    var listProvider: MutableList<ResponseUserMitra> = ArrayList()
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (statusBarHeight > 0) {
            binding.statusbar.layoutParams.height = resources.getDimensionPixelSize(statusBarHeight)
        }
        val navigationBarHeight =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (navigationBarHeight > 0) {
            binding.bottomNavBar.layoutParams.height =
                resources.getDimensionPixelSize(navigationBarHeight)
        }
        setupButton()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Harap tunggu...")

        val idTour = arguments?.getString(HomeFragment.ID_TOUR)
        val tourName = arguments?.getString(HomeFragment.TOUR_NAME)
        val lat = arguments?.getString(HomeFragment.LAT)
        val longi = arguments?.getString(HomeFragment.LONG)
        val address = arguments?.getString(HomeFragment.ADDRESS)
        val img = arguments?.getString(HomeFragment.IMG_URL)
        val desc = arguments?.getString(HomeFragment.DESC)

        Glide.with(requireContext())
            .load(img)
            .transform(CenterCrop())
            .into(binding.ivTour)
        binding.tvTourName.text = tourName
        binding.tvDesc.text = desc

        val POIBundle = Bundle()
        POIBundle.putString(HomeFragment.ID_TOUR, idTour)
        POIBundle.putString(HomeFragment.TOUR_NAME, tourName)
        POIBundle.putString(HomeFragment.LAT, lat)
        POIBundle.putString(HomeFragment.LONG, longi)
        POIBundle.putString(HomeFragment.ADDRESS, address)
        POIBundle.putString(HomeFragment.IMG_URL, img)

        binding.btnNext.setOnClickListener {
            checkUser(POIBundle)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getPOI() {
        val tourId = arguments?.getString(HomeFragment.ID_TOUR)?.toInt()
        mapsViewModel.getPoiList(tourId!!)
        mapsViewModel.poiList.observe(viewLifecycleOwner) { poiList ->
            when (poiList.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (!poiList.data.isNullOrEmpty()) {
                        listFacility.clear()
                        listFacility.addAll(poiList.data)
                        binding.tvDescFacility.text = "${listFacility.size} Fasilitas Umum"

                    } else {
                        Toast.makeText(requireContext(), "Marker masih kosong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), poiList.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }

    }

    private fun getPOIUser() {
        val idTour = arguments?.getString(HomeFragment.ID_TOUR)
        mapsViewModel.getPoiListMitra()
        mapsViewModel.poiListMitra.observe(viewLifecycleOwner) { poiListMitra ->
            when (poiListMitra.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (!poiListMitra.data.isNullOrEmpty()) {
                        listProvider.clear()
                        for (i in poiListMitra.data) {
                            if (i.kodeWisata == idTour && i.role == "2") {
                                listProvider.add(i)
                            }
                        }
                        binding.tvDescProvider.text = "${listProvider.size} Penyedia Layanan"

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Marker mitra masih kosong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), poiListMitra.message, Toast.LENGTH_SHORT)
                        .show()
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }

    }

    private fun setupButton() {
        binding.apply {
            btnFacility.setOnClickListener {
                btnDesc.strokeColor = ColorStateList.valueOf(Color.rgb(0, 173, 181))
                btnDesc.strokeWidth = 3
                btnDesc.setTextColor(Color.parseColor("#00ADB5"))
                btnDesc.setBackgroundColor(Color.parseColor("#EEEEEE"))

                btnFacility.strokeWidth = 0
                btnFacility.setTextColor(Color.parseColor("#EEEEEE"))
                btnFacility.setBackgroundColor(Color.parseColor("#00ADB5"))

                scrollview.visibility = View.GONE

                tvHave.visibility = View.VISIBLE
                tvDescFacility.visibility = View.VISIBLE
                tvDescProvider.visibility = View.VISIBLE
                icFacility.visibility = View.VISIBLE
                icProvider.visibility = View.VISIBLE

                getPOI()
                getPOIUser()
            }
            btnDesc.setOnClickListener {
                btnFacility.strokeColor = ColorStateList.valueOf(Color.rgb(0, 173, 181))
                btnFacility.strokeWidth = 3
                btnFacility.setTextColor(Color.parseColor("#00ADB5"))
                btnFacility.setBackgroundColor(Color.parseColor("#EEEEEE"))

                btnDesc.strokeWidth = 0
                btnDesc.setTextColor(Color.parseColor("#EEEEEE"))
                btnDesc.setBackgroundColor(Color.parseColor("#00ADB5"))

                scrollview.visibility = View.VISIBLE

                tvHave.visibility = View.GONE
                tvDescFacility.visibility = View.GONE
                tvDescProvider.visibility = View.GONE
                icFacility.visibility = View.GONE
                icProvider.visibility = View.GONE
            }
        }
    }

    private fun checkUser(POIBundle: Bundle) {
        val preference = requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val username = preference.getString(USERNAME, DEFAULT_VALUE)
        if (username == DEFAULT_VALUE) {
            AlertDialog.Builder(requireContext())
                .setTitle("Pesan")
                .setMessage("Untuk melihat wisata anda harus login terlebih dahulu.")
                .setPositiveButton("Ok") { positiveButton, _ ->
                    positiveButton.dismiss()
                    findNavController().navigate(R.id.action_descFragment_to_loginFragment)
                }
                .setNegativeButton("Batal") { negativeButton, _ ->
                    negativeButton.dismiss()
                }
                .show()
        } else if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Pesan")
                .setMessage("Akses lokasi belum diijinkan, beri ijin lokasi terlebih dahulu")
                .setPositiveButton("Ok") { positiveButton, _ ->
                    positiveButton.dismiss()
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        HomeFragment.LOCATION_REQUEST_CODE
                    )
                }
                .show()
        } else {
            findNavController().navigate(R.id.action_descFragment_to_mapsFragment, POIBundle)
        }
    }
}