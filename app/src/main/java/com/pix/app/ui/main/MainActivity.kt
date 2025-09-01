package com.pix.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pix.app.R
import com.pix.app.databinding.ActivityMainBinding
import com.pix.app.fragments.ChatFragment
import com.pix.app.fragments.DiscoverFragment
import com.pix.app.fragments.HomeFragment
import com.pix.app.fragments.ProfileFragment
import com.pix.app.ui.camera.CameraActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        setupBottomNavigation()
        
        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_discover -> {
                    loadFragment(DiscoverFragment())
                    true
                }
                R.id.nav_create -> {
                    // Open camera activity
                    startActivity(Intent(this, CameraActivity::class.java))
                    false // Don't select this item
                }
                R.id.nav_inbox -> {
                    loadFragment(ChatFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    
    override fun onResume() {
        super.onResume()
        // Deselect create button when returning from camera
        binding.bottomNavigation.menu.findItem(R.id.nav_create).isChecked = false
    }
}