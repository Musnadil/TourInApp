package com.indexdev.tourin.ui.listratefacility

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
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
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.response.Data
import com.indexdev.tourin.databinding.FragmentListRateFacilityBinding
import com.indexdev.tourin.ui.listratefacility.ratefacility.RateFacilityDialogFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListRateFacilityFragment : Fragment() {
    private var _binding: FragmentListRateFacilityBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListRateFacilityViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private val listFacilityRate: MutableList<Data> = ArrayList()
    private lateinit var listRateFacilityAdapter: ListRateFacilityAdapter
    private var idUser: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRateFacilityBinding.inflate(layoutInflater, container, false)
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

        val preference = requireContext().getSharedPreferences(
            SplashScreenFragment.SHARED_PREF,
            Context.MODE_PRIVATE
        )
        idUser = preference.getString(
            SplashScreenFragment.ID_USER,
            SplashScreenFragment.DEFAULT_VALUE
        )
        if (idUser != DEFAULT_VALUE){
            getListFacilityRate(idUser.toString().toInt())
        } else{
            findNavController().navigate(R.id.splashScreenFragment)
        }
        detailFacilityRate()
    }


    private fun detailFacilityRate() {
        listRateFacilityAdapter =
            ListRateFacilityAdapter(object : ListRateFacilityAdapter.OnClickListener {
                override fun onClickItem(data: Data) {
//                    Toast.makeText(requireContext(), "${data.rateValue}", Toast.LENGTH_SHORT).show()
                    val dialogFragment = RateFacilityDialogFragment(
                        data.idRateFasilitas.toInt(),
                        data.namaFasilitas,
                        data.namaWisata,
                        data.rateValue,
                        data.keterangan,
                        refreshList = {
                            getListFacilityRate(idUser.toString().toInt())
                            binding.rvListRate.adapter?.notifyDataSetChanged()
                        }
                    )
                    activity?.let { dialogFragment.show(it.supportFragmentManager, null) }
                }
            })
        binding.rvListRate.adapter = listRateFacilityAdapter
    }

    private fun getListFacilityRate(id: Int) {
        binding.rvListRate.visibility = View.GONE
        progressDialog.show()
        viewModel.rateFacilityList.removeObservers(viewLifecycleOwner)
        viewModel.getRateFacilityList(id)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.rateFacilityList.observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        when (it.data?.code) {
                            200 -> {
                                listFacilityRate.clear()
                                listFacilityRate.addAll(it.data.data)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progressDialog.dismiss()
                                    listRateFacilityAdapter.submitData(listFacilityRate)
                                    binding.rvListRate.visibility = View.VISIBLE
                                }, 1000)
                                viewModel.rateFacilityList.removeObservers(viewLifecycleOwner)
                            }
                            404 -> {
                                progressDialog.dismiss()
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Pesan")
                                    .setMessage("Belum ada fasilitas wisata yang perlu di beri rating")
                                    .setPositiveButton("Ok") { positiveButton, _ ->
                                        positiveButton.dismiss()
                                    }
                                    .show()
                                binding.rvListRate.visibility = View.GONE
                                viewModel.rateFacilityList.removeObservers(viewLifecycleOwner)

                            }
                        }
                    }
                    ERROR -> {
                        progressDialog.dismiss()
                        binding.rvListRate.visibility = View.GONE
                        viewModel.rateFacilityList.removeObservers(viewLifecycleOwner)
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
                        binding.rvListRate.visibility = View.GONE
                    }
                }
            }
        }, 1000)
    }
}