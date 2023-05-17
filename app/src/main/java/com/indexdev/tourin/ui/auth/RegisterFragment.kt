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
import com.indexdev.tourin.ui.alertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    val bundle = Bundle()

    companion object {
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
                binding.usernameContainer.error = "Kolom nama pengguna tidak boleh kosong"
            } else if (binding.etEmail.text.isNullOrEmpty()) {
                binding.etEmail.error = "Kolom Email tidak boleh kosong"
            } else if (binding.etPassword.text.isNullOrEmpty()) {
                binding.etPassword.error = "Kolom password tidak boleh kosong"
            } else if (binding.etConfirmPassword.text.isNullOrEmpty()) {
                binding.confirmPasswordContainer.error =
                    "Kolom konfirmasi password tidak boleh kosong"
            } else if (binding.etPassword.text.toString().length <= 6) {
                binding.passwordContainer.error = "Kata sandi harus lebih dari 6 karakter"
            } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.confirmPasswordContainer.error = "Konfirmasi password tidak sama"
                binding.etConfirmPassword.requestFocus()
            } else {
                bundle.putString(EMAIL, binding.etEmail.text.toString())
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
                            AlertDialog.Builder(context)
                                .setTitle("Berhasil daftar")
                                .setMessage("Anda telah berhasil mendaftar")
                                .setCancelable(false)
                                .setPositiveButton("OK") { positive, _ ->
                                    positive.dismiss()
                                    findNavController().navigate(
                                        R.id.action_registerFragment_to_loginFragment,
                                        bundle
                                    )
                                }
                                .show()
                        }
                        401 -> {
                            alertDialog(
                                requireContext(),
                                "Gagal mendaftar",
                                "Email yang anda masukan sudah digunakan",
                                binding.etEmail.requestFocus()
                            )
                        }
                        402 -> {
                            alertDialog(
                                requireContext(),
                                "Gagal mendaftar",
                                "Email yang anda masukan tidak valid",
                                binding.etEmail.requestFocus()
                            )
                        }
                        403 -> {
                            alertDialog(
                                requireContext(),
                                "Gagal mendaftar",
                                "Domain email yang anda masukan tidak ditemukan",
                                binding.etEmail.requestFocus()
                            )
                        }
                    }
                }
                ERROR -> {
                    binding.loading.root.visibility = View.GONE
                    alertDialog(
                        requireContext(),
                        "Pesan",
                        resources.message ?: getString(R.string.error)
                    )
                }
            }
        }
    }
}