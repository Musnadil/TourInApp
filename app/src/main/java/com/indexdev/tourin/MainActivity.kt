package com.indexdev.tourin

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.indexdev.tourin.databinding.ActivityMainBinding
import com.indexdev.tourin.ui.lightStatusBar
import com.indexdev.tourin.ui.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
@SuppressLint("InlinedApi")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listDestination = listOf(
        R.id.splashScreenFragment,
        R.id.firstOnBoardingFragment,
        R.id.secondOnBoardingFragment,
        R.id.thirdOnBoardingFragment,
    )
    private val listTransparent = listOf(
        R.id.loginFragment,
        R.id.registerFragment,
        R.id.forgotPasswordFragment,
        R.id.verifyOtpFragment,
        R.id.newPasswordFragment
    )

    private val listWhiteTheme = listOf(
        R.id.homeFragment,
        R.id.mapsFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragment_container)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in listDestination -> {
                    hideSystemUI()
                }
                in listTransparent -> {
                    showSystemUI()
                    lightStatusBar(window, false)
                    window.navigationBarColor = Color.parseColor("#80000000")
                }
                in listWhiteTheme -> {
                    showSystemUI()
                    lightStatusBar(window)
                    window.navigationBarColor = Color.parseColor("#EEEEEE")
                }
                else -> {
                    showSystemUI()
                    lightStatusBar(window)
                }
            }
        }

        setFullScreen(window)
        lightStatusBar(window)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}