package com.indexdev.tourin.ui.desc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentDescBinding
import com.indexdev.tourin.ui.home.HomeFragment

class DescFragment : Fragment() {
    private var _binding: FragmentDescBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idTour = arguments?.getString(HomeFragment.ID_TOUR)
        val tourName = arguments?.getString(HomeFragment.TOUR_NAME)
        val lat = arguments?.getString(HomeFragment.LAT)
        val longi = arguments?.getString(HomeFragment.LONG)
        val address = arguments?.getString(HomeFragment.ADDRESS)
        val img = arguments?.getString(HomeFragment.IMG_URL)
        val POIBundle = Bundle()

        POIBundle.putString(HomeFragment.ID_TOUR, idTour)
        POIBundle.putString(HomeFragment.TOUR_NAME, tourName)
        POIBundle.putString(HomeFragment.LAT, lat)
        POIBundle.putString(HomeFragment.LONG, longi)
        POIBundle.putString(HomeFragment.ADDRESS, address)
        POIBundle.putString(HomeFragment.IMG_URL, img)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_descFragment_to_mapsFragment,POIBundle)
        }
    }
}