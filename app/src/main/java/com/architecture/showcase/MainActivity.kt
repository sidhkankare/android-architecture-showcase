package com.architecture.showcase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.architecture.showcase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Architecture Pattern Selection Screen
 *
 * This is the launcher activity that allows users to choose
 * which architecture pattern they want to explore:
 * - MVP (Model-View-Presenter)
 * - MVVM (Model-View-ViewModel)
 * - MVI (Model-View-Intent)
 * - Clean Architecture
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        // Set up click listeners for each architecture pattern card
        binding.cardMvp.setOnClickListener {
            // TODO: Navigate to MVP module
            showComingSoon("MVP")
        }

        binding.cardMvvm.setOnClickListener {
            // TODO: Navigate to MVVM module
            showComingSoon("MVVM")
        }

        binding.cardMvi.setOnClickListener {
            // TODO: Navigate to MVI module
            showComingSoon("MVI")
        }

        binding.cardClean.setOnClickListener {
            // TODO: Navigate to Clean Architecture module
            showComingSoon("Clean Architecture")
        }
    }

    private fun showComingSoon(patternName: String) {
        Toast.makeText(
            this,
            "$patternName implementation coming soon!",
            Toast.LENGTH_SHORT
        ).show()
    }
}
