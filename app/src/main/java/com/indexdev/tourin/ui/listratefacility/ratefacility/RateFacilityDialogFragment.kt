package com.indexdev.tourin.ui.listratefacility.ratefacility

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.RateFacilityRequest
import com.indexdev.tourin.databinding.FragmentRateFacilityDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFacilityDialogFragment(
    private val idRateFacility: Int,
    private val facilityName: String,
    private val tourName: String,
    private val rateValue: String?,
    private val desc: String?,
    private val refreshList: () -> Unit
) : DialogFragment() {
    private var _binding: FragmentRateFacilityDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog
    private val viewModel: RateFacilityDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.RateDialogAnimation
        dialog?.setCanceledOnTouchOutside(true)
        _binding = FragmentRateFacilityDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Harap tunggu...")
        binding.tvTextQuestion.text =
            "Seberapa puaskah anda setelah menggunakan $facilityName pada $tourName?"
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSend.setOnClickListener {
            sendRate()
        }
        if (rateValue != null) {
            binding.btnSend.isEnabled = false
            binding.ratingBar.rating = rateValue.toFloat()
            binding.etDesc.isEnabled = false
            binding.etDesc.setText(desc)
        }
    }

    private fun sendRate() {
        progressDialog.show()
        val request = RateFacilityRequest(
            binding.ratingBar.rating.toFloat(),
            binding.etDesc.text.toString()
        )
        viewModel.responseRateFacility.removeObservers(viewLifecycleOwner)
        viewModel.rateFacility(idRateFacility, request)
        viewModel.responseRateFacility.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    when (it.data?.code) {
                        200 -> {
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                dialog?.dismiss()
                                refreshList.invoke()
                                Toast.makeText(
                                    requireContext(),
                                    "Terimakasih telah memberi nilai pada $facilityName",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }, 3000)
                            viewModel.responseRateFacility.removeObservers(viewLifecycleOwner)
                        }
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    viewModel.responseRateFacility.removeObservers(viewLifecycleOwner)
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