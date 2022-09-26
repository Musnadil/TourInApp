package com.indexdev.tourin.ui.auth

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    val bundle = Bundle()

    companion object{
        const val EMAIL = "email"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        register()
        resultRegister()
    }

    private fun register() {
        binding.btnRegist.setOnClickListener {
            binding.usernameContainer.error = null
            binding.emailContainer.error = null
            binding.passwordContainer.error = null
            binding.confirmPasswordContainer.error = null

            if (binding.etUsername.text.isNullOrEmpty()) {
                binding.usernameContainer.error = "You must fill in the username field!"
            } else if (binding.etEmail.text.isNullOrEmpty()) {
                binding.etEmail.error = "You must fill in the email field!"
            } else if (binding.etPassword.text.isNullOrEmpty()) {
                binding.etPassword.error = "You must fill in the password field!"
            } else if (binding.etConfirmPassword.text.isNullOrEmpty()) {
                binding.confirmPasswordContainer.error = "You have to confirm the password!"
            } else if (binding.etPassword.text.toString().length <= 6) {
                binding.passwordContainer.error = "Password must be more than 6 characters!"
            } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.confirmPasswordContainer.error = "Password confirmation failed!"
                binding.etConfirmPassword.requestFocus()
            } else {
                bundle.putString(EMAIL,binding.etEmail.text.toString())
                val registerRequest = RegisterRequest(
                    binding.etEmail.text.toString(),
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
                )
                authViewModel.authRegister(registerRequest)
            }
        }
    }

    private fun resultRegister() {
        authViewModel.register.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                LOADING -> {
                    binding.loading.root.visibility = View.VISIBLE
                }
                SUCCESS -> {
                    binding.loading.root.visibility = View.GONE
                    when (resources.data?.code) {
                        200 -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Successful Registration")
                                .setMessage(resources.data.message)
                                .setPositiveButton("OK") { positive, _ ->
                                    positive.dismiss()
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment,bundle)
                                }
                                .show()
                        }
                        401 -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Registration failed")
                                .setMessage(resources.data.message)
                                .setPositiveButton("OK") { positive, _ ->
                                    positive.dismiss()
                                    binding.etEmail.requestFocus()
                                }
                                .show()
                        }
                        402 ->{
                            AlertDialog.Builder(requireContext())
                                .setTitle("Registration failed")
                                .setMessage(resources.data.message)
                                .setPositiveButton("OK") { positive, _ ->
                                    positive.dismiss()
                                    binding.etEmail.requestFocus()
                                }
                                .show()
                        }
                    }
                }
                ERROR -> {
                    binding.loading.root.visibility = View.GONE
                    AlertDialog.Builder(requireContext())
                        .setTitle("Message")
                        .setMessage(resources.message ?: "error")
                        .setPositiveButton("OK") { positiveButton, _ ->
                            positiveButton.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}