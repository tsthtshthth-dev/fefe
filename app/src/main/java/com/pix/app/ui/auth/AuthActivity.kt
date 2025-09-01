package com.pix.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pix.app.R
import com.pix.app.databinding.ActivityAuthBinding
import com.pix.app.ui.main.MainActivity
import com.pix.app.utils.PreferenceManager

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    private lateinit var preferenceManager: PreferenceManager
    private var isLoginMode = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        
        preferenceManager = PreferenceManager(this)
        
        setupUI()
        setupClickListeners()
    }
    
    private fun setupUI() {
        updateUIMode()
    }
    
    private fun setupClickListeners() {
        binding.btnPrimary.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performSignup()
            }
        }
        
        binding.tvToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUIMode()
        }
        
        binding.tvForgotPassword.setOnClickListener {
            // Handle forgot password
            Toast.makeText(this, "Forgot password functionality", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateUIMode() {
        if (isLoginMode) {
            // Login mode
            binding.tvTitle.text = getString(R.string.login)
            binding.btnPrimary.text = getString(R.string.login)
            binding.tvToggleMode.text = getString(R.string.dont_have_account)
            binding.etFullName.visibility = android.view.View.GONE
            binding.etConfirmPassword.visibility = android.view.View.GONE
            binding.tvForgotPassword.visibility = android.view.View.VISIBLE
        } else {
            // Signup mode
            binding.tvTitle.text = getString(R.string.create_account)
            binding.btnPrimary.text = getString(R.string.create_account)
            binding.tvToggleMode.text = getString(R.string.already_have_account)
            binding.etFullName.visibility = android.view.View.VISIBLE
            binding.etConfirmPassword.visibility = android.view.View.VISIBLE
            binding.tvForgotPassword.visibility = android.view.View.GONE
        }
    }
    
    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        if (validateLoginInput(email, password)) {
            // Simulate login (replace with actual API call)
            simulateLogin(email, password)
        }
    }
    
    private fun performSignup() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        
        if (validateSignupInput(fullName, email, password, confirmPassword)) {
            // Simulate signup (replace with actual API call)
            simulateSignup(fullName, email, password)
        }
    }
    
    private fun validateLoginInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            return false
        }
        
        return true
    }
    
    private fun validateSignupInput(fullName: String, email: String, password: String, confirmPassword: String): Boolean {
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            return false
        }
        
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        
        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Confirm password is required"
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            return false
        }
        
        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            return false
        }
        
        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match"
            return false
        }
        
        return true
    }
    
    private fun simulateLogin(email: String, password: String) {
        // Simulate API call delay
        binding.btnPrimary.isEnabled = false
        binding.btnPrimary.text = getString(R.string.loading)
        
        // Simulate successful login after 1 second
        binding.btnPrimary.postDelayed({
            // Save user data
            preferenceManager.setLoggedIn(true)
            preferenceManager.setUserData(
                userId = "user_123",
                username = email.substringBefore("@"),
                email = email,
                fullName = "User Name"
            )
            
            // Navigate to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }
    
    private fun simulateSignup(fullName: String, email: String, password: String) {
        // Simulate API call delay
        binding.btnPrimary.isEnabled = false
        binding.btnPrimary.text = getString(R.string.loading)
        
        // Simulate successful signup after 1 second
        binding.btnPrimary.postDelayed({
            // Save user data
            preferenceManager.setLoggedIn(true)
            preferenceManager.setUserData(
                userId = "user_${System.currentTimeMillis()}",
                username = email.substringBefore("@"),
                email = email,
                fullName = fullName
            )
            
            // Navigate to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }
}