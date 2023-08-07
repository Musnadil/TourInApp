package com.indexdev.tourin.ui.auth

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.databinding.FragmentLoginBinding
import com.indexdev.tourin.ui.alertDialog
import com.indexdev.tourin.ui.auth.RegisterFragment.Companion.EMAIL
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.ID_USER
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.SHARED_PREF
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.TOKEN
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.USERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = arguments?.getString(EMAIL)
        binding.etEmail.setText(email)
        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        login()
        resultLogin()
        binding.btnForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }


    private fun login() {
        binding.btnLogin.setOnClickListener {
            binding.emailContainer.error = null
            binding.passwordContainer.error = null
            if (binding.etEmail.text.isNullOrEmpty()) {
                binding.emailContainer.error = "Kolom Email tidak boleh kosong"
            } else if (binding.etPassword.text.isNullOrEmpty()) {
                binding.passwordContainer.error = "Kolom Kata Sandi tidak boleh kosong"
            } else if (binding.etPassword.text.toString().length <= 5) {
                binding.passwordContainer.error = "Kata sandi minimal berisi 6 karakter"
            } else {
                binding.loading.root.visibility = View.VISIBLE
                val loginRequest = LoginRequest(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
                authViewModel.authLogin(loginRequest)
            }
        }
    }

    private fun resultLogin() {
        val preference = requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val userEditor = preference.edit()
        authViewModel.login.observe(viewLifecycleOwner) { resources ->
            Handler(Looper.getMainLooper()).postDelayed({
                when (resources.status) {
                    LOADING -> {
                        binding.loading.root.visibility = View.VISIBLE
                    }
                    SUCCESS -> {
                        binding.loading.root.visibility = View.GONE
                        when (resources.data?.code) {
                            200 -> {
                                userEditor.putString(ID_USER, resources.data.id)
                                userEditor.putString(USERNAME, resources.data.username)
                                userEditor.putString(TOKEN, resources.data.token)
                                userEditor.apply()
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                            405 -> {
                                alertDialog(
                                    requireContext(),
                                    "Gagal masuk",
                                    "Password salah"
                                )
                            }
                            404 -> {
                                alertDialog(
                                    requireContext(),
                                    "Gagal masuk",
                                    "Email belum terdaftar"
                                )
                            }
                            402 -> {
                                alertDialog(
                                    requireContext(),
                                    "Gagal masuk",
                                    "Email yang anda masukan tidak ditemukan"
                                )
                            }
                        }
                    }
                    ERROR -> {
                        binding.loading.root.visibility = View.GONE
                        alertDialog(
                            requireContext(),
                            "Pesan",
                            "Harap tunggu sebentar, dan coba lagi"
//                            resources.message ?: getString(
//                                R.string.error
//                            )
                        )
                    }
                }
            }, 1000)
        }
    }
}