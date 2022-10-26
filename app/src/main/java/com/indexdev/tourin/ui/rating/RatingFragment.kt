package com.indexdev.tourin.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentRatingBinding

class RatingFragment : DialogFragment() {
    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        _binding = FragmentRatingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setOnClickListener {
            dialog?.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}