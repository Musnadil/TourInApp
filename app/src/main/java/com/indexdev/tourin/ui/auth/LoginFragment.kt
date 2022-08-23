package com.indexdev.tourin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            binding.emailContainer.error = null
            binding.passwordContainer.error = null
            if (binding.etEmail.text.isNullOrEmpty()) {
                binding.emailContainer.error = "You must fill in the password field!"
            } else if (binding.etPassword.text.isNullOrEmpty()) {
                binding.passwordContainer.error = "You must fill in the password field!"
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }
}