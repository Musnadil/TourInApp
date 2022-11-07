package com.indexdev.tourin.ui.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import com.indexdev.tourin.databinding.FragmentEditAccountDialogBinding
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.ID_USER
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.SHARED_PREF
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.USERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAccountDialogFragment(private val usernameUpdate:() ->Unit) : DialogFragment() {
    private var _binding: FragmentEditAccountDialogBinding? = null
    private val binding get() = _binding!!
    private val editViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.setCanceledOnTouchOutside(false)
        _binding = FragmentEditAccountDialogBinding.inflate(layoutInflater, container, false)
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
        val preference = requireContext().getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
        val usernameOld = preference.getString(USERNAME, DEFAULT_VALUE)
        val idUser = preference.getString(ID_USER, DEFAULT_VALUE)
        val editPrefUsername = preference.edit()
        binding.etUsername.setText(usernameOld)
        binding.btnUpdate.setOnClickListener {
            if (usernameOld != DEFAULT_VALUE && idUser != DEFAULT_VALUE){
                if (!binding.etUsername.text.isNullOrEmpty()){
                    val newUsername = UpdateUserRequest(binding.etUsername.text.toString())
                    editViewModel.editUsername(idUser.toString().toInt(), newUsername)
                }else{
                    binding.usernameContainer.error ="Username cannot be empty"
                }
            }
        }
        editViewModel.username.observe(viewLifecycleOwner){username ->
            when(username.status){
                SUCCESS -> {
                    editPrefUsername.putString(USERNAME, binding.etUsername.text.toString())
                    editPrefUsername.apply()
                    usernameUpdate.invoke()
                    dialog?.dismiss()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "${username.message}", Toast.LENGTH_SHORT).show()
                }
                LOADING ->{}
            }
        }

    }
}