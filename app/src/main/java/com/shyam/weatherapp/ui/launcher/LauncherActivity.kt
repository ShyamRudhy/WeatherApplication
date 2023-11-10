package com.shyam.weatherapp.ui.launcher

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.shyam.weatherapp.MainActivity
import com.shyam.weatherapp.base.BaseActivity
import com.shyam.weatherapp.databinding.ActivityLauncherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    private var _binding: ActivityLauncherBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeNavigationFlow()

        lifecycleScope.launch {
            viewModel.getNavigateNextFlow()
        }

    }

    private fun observeNavigationFlow() {
        viewModel.navigationListener.observe(this) { navigationState ->
            when (navigationState) {
                LauncherViewModel.NavigationState.ON_BOARDING_PAGE -> {
                }
                LauncherViewModel.NavigationState.IDLE -> {
                }
                LauncherViewModel.NavigationState.HOME_PAGE -> {
                    navigateToHomePage()
                }
                else -> {
                    navigateToHomePage()
                }
            }

        }
    }

    private fun navigateToHomePage() {
        val logoImage = binding.logoImage
        // Navigating to home page with transition
        logoImage.animate()
            .translationX(logoImage.height.toFloat())
            .alpha(0.0f)
            .setDuration(2000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                    finish()
                }
            })
    }
}