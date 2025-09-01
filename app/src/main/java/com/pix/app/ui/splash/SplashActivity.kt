package com.pix.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.pix.app.R
import com.pix.app.ui.auth.AuthActivity
import com.pix.app.ui.main.MainActivity
import com.pix.app.utils.PreferenceManager

class SplashActivity : AppCompatActivity() {
    
    private val splashTimeOut: Long = 2000 // 2 seconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLogin()
        }, splashTimeOut)
    }
    
    private fun checkUserLogin() {
        val preferenceManager = PreferenceManager(this)
        
        if (preferenceManager.isLoggedIn()) {
            // User is logged in, go to main activity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in, go to auth activity
            startActivity(Intent(this, AuthActivity::class.java))
        }
        
        finish()
    }
}