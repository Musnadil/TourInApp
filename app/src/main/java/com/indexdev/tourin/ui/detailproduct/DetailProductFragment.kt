package com.indexdev.tourin.ui.detailproduct

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.indexdev.tourin.data.api.Status
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.databinding.FragmentDetailProdukBinding
import com.indexdev.tourin.ui.currency
import com.indexdev.tourin.ui.number
import com.indexdev.tourin.ui.productpartner.ProductPartnerFragment
import com.indexdev.tourin.ui.productpartner.ProductPartnerFragment.Companion.ID_PRODUK
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProdukBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog
    private val viewModel : DetailProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProdukBinding.inflate(layoutInflater, container, false)
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
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnOrder.setOnClickListener {
            val nomor = arguments?.getString(ProductPartnerFragment.NUMBER_PHONE)
                ?.let { it1 -> number(it1) }
            val text = "Halo, saya pengguna apliaksi TourIn apakah produk ${binding.tvProductName.text} masih tersedia?"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$nomor&text=$text"))
            startActivity(intent)
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Harap tunggu...")

        getProductById()

    }

    private fun getProductById() {
        arguments?.getString(ID_PRODUK)?.let { viewModel.getProductById(it.toInt()) }
        viewModel.responseProductById.observe(viewLifecycleOwner) {
                when (it.status) {
                SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data?.idProduk != null) {
                        Glide.with(requireContext())
                            .load("http://tourin.musnadil.my.id/gambar/${it.data.gambar}")
                            .transform(CenterCrop())
                            .into(binding.ivProduct)
                        binding.tvProductName.text = it.data.namaProduk
                        binding.tvPrice.text = currency(it.data.harga.toInt())
                        binding.tvDesc.text = it.data.deskripsi

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Produk tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(it.message ?: "Error")
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
    }
}