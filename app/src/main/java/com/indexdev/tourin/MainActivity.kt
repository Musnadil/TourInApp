package com.indexdev.tourin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        R.id.loginFragment,
        R.id.registerFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragment_container)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listDestination) {
                hideSystemUI()
            } else {
                showSystemUI()
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