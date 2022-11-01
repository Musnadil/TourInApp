package com.indexdev.tourin.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentRatingBinding
import com.indexdev.tourin.ui.home.HomeFragment.Companion.TOUR_NAME
import com.indexdev.tourin.ui.initiateNotify
import com.indexdev.tourin.ui.maps.MapsFragment.Companion.NOTIF_ID

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
        val manager = NotificationManagerCompat.from(requireContext())
        manager.cancel(NOTIF_ID)

        binding.btnSend.setOnClickListener {
            dialog?.dismiss()
            initiateNotify = false
            findNavController().clearBackStack(R.id.homeFragment)

        }
        binding.tv.text = "Give a rating for ${arguments?.getString(TOUR_NAME)}"

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