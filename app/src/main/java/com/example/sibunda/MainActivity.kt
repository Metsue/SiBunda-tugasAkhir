package com.example.sibunda

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sibunda.core.utils.ThemeManager
import com.example.sibunda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        applyGlobalTheme()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.loginFragment,
                R.id.registerFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }

            binding.root.post {
                applyGlobalTheme()
                applyThemeToCurrentFragment()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.root.post {
            applyGlobalTheme()
            applyThemeToCurrentFragment()
        }
    }

    private fun applyGlobalTheme() {
        ThemeManager.applyToActivity(
            activity = this,
            rootView = binding.main,
            bottomNavigationView = binding.bottomNavigation
        )
    }

    private fun applyThemeToCurrentFragment() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        val currentFragment = navHostFragment
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull()

        val fragmentView = currentFragment?.view ?: return

        ThemeManager.applyToScreen(this, fragmentView)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}