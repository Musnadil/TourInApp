package com.indexdev.tourin.ui.productpartner

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.databinding.FragmentProductPartnerBinding
import com.indexdev.tourin.ui.maps.MapsFragment
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProductPartnerFragment : Fragment() {
    private var _binding: FragmentProductPartnerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductPartnerViewModel by viewModels()
    private var partnerId: Int = 0
    private lateinit var progressDialog: ProgressDialog
    private val bundle = Bundle()

    companion object {
        const val IMAGE_STORE = "IMAGE_STORE"
        const val BUSINESS_NAME = "BUSINESS_NAME"
        const val BUSINESS_OWNER = "BUSINESS_OWNER"
        const val NUMBER_PHONE = "NUMBER_PHONE"
        const val ADDRESS = "ADDRESS"
        const val LAT = "LAT"
        const val LONG = "LONG"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPartnerBinding.inflate(layoutInflater, container, false)
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
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Harap tunggu...")

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        partnerId = arguments?.getString(MapsFragment.PARTNER_ID).toString().toInt()
        getDetailPartnerProfile()
        binding.cardProfile.setOnClickListener {
            //ke screen lalu bawa bundle
            /*
            * findnavcontroller navigate product detail profile, bundle
            * */
            bundle
        }
    }

    private fun getDetailPartnerProfile() {
        progressDialog.show()
        viewModel.getUserPartnerById(partnerId)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.responseUserPartnerById.observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        progressDialog.dismiss()
                        when (it.data?.code) {
                            200 -> {
                                bundle.putString(IMAGE_STORE, it.data.userMitraById.gambar)
                                bundle.putString(BUSINESS_NAME, it.data.userMitraById.namaUsaha)
                                bundle.putString(BUSINESS_OWNER, it.data.userMitraById.namaPemilik)
                                bundle.putString(NUMBER_PHONE, it.data.userMitraById.noPonsel)
                                bundle.putString(ADDRESS, it.data.userMitraById.alamat)
                                bundle.putString(LAT, it.data.userMitraById.lat)
                                bundle.putString(LONG, it.data.userMitraById.longi)
                                binding.tvBusinessName.text = it.data.userMitraById.namaUsaha
                                binding.tvBusinessNameCard.text = it.data.userMitraById.namaUsaha
                            }
                            404 -> {
                                Toast.makeText(
                                    requireContext(),
                                    "User tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                    ERROR -> {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setTitle("Pesan")
                            .setMessage(it.message ?: "error")
                            .setPositiveButton("Ok") { positiveButton, _ ->
                                positiveButton.dismiss()
                            }
                            .show()
                    }
                    LOADING -> {
                        progressDialog.show()
                    }
                }
            }
        }, 1000)
    }
}