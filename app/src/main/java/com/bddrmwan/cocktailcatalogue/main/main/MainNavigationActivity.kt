package com.bddrmwan.cocktailcatalogue.main.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.ActivityMainNavigationBinding
import com.bddrmwan.cocktailcatalogue.main.extensions.gone
import com.bddrmwan.cocktailcatalogue.main.extensions.toast
import com.bddrmwan.cocktailcatalogue.main.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainNavigationBinding
    private var doubleBackToExitPressedOnce = false
    private var isRootNavigation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.let { binding.bottomNav.setupWithNavController(navController) }
        handleBottomNav()

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            handleBottomNav()
            isRootNavigation = when (destination.id) {
                R.id.homeFragment, R.id.bookmarkFragment -> {
                    binding.bottomNav.visible()
                    true
                }
                else -> {
                    binding.bottomNav.gone()
                    false
                }
            }
        }
    }

    fun hideBottomNav() {
        binding.bottomNav.gone()
    }

    fun showBottomNav() {
        binding.bottomNav.visible()
    }

    private fun handleBottomNav() {
        binding.bottomNav.menu.forEach {
            it.isEnabled = !it.isChecked
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce || !isRootNavigation) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        toast(getString(R.string.press_back_again))
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}