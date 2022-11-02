package com.indexdev.tourin.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentEditAccountDialogBinding

class EditAccountDialogFragment : DialogFragment() {
    private var _binding: FragmentEditAccountDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.setCanceledOnTouchOutside(false)
        _binding = FragmentEditAccountDialogBinding.inflate(layoutInflater,container,false)
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
    }
}