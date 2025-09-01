package com.pix.app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pix.app.R
import com.pix.app.databinding.ActivitySettingsBinding
import com.pix.app.ui.auth.AuthActivity
import com.pix.app.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        
        preferenceManager = PreferenceManager(this)
        
        setupToolbar()
        setupClickListeners()
        loadSettings()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
    }
    
    private fun setupClickListeners() {
        // Account Settings
        binding.layoutEditProfile.setOnClickListener {
            // Navigate to edit profile
            // TODO: Implement edit profile screen
        }
        
        binding.layoutAccountPrivacy.setOnClickListener {
            // Navigate to account privacy settings
            // TODO: Implement privacy settings
        }
        
        binding.layoutChangePassword.setOnClickListener {
            // Navigate to change password
            // TODO: Implement change password screen
        }
        
        binding.layoutBlockedUsers.setOnClickListener {
            // Navigate to blocked users list
            // TODO: Implement blocked users screen
        }
        
        // Privacy Settings
        binding.layoutPrivateAccount.setOnClickListener {
            binding.switchPrivateAccount.toggle()
            handlePrivateAccountToggle()
        }
        
        binding.switchPrivateAccount.setOnCheckedChangeListener { _, isChecked ->
            handlePrivateAccountToggle()
        }
        
        binding.layoutWhoCanMessage.setOnClickListener {
            // Show message privacy options
            showMessagePrivacyOptions()
        }
        
        binding.layoutWhoCanComment.setOnClickListener {
            // Show comment privacy options
            showCommentPrivacyOptions()
        }
        
        // Notifications
        binding.layoutPushNotifications.setOnClickListener {
            binding.switchPushNotifications.toggle()
            handlePushNotificationsToggle()
        }
        
        binding.switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            handlePushNotificationsToggle()
        }
        
        binding.layoutLikesComments.setOnClickListener {
            binding.switchLikesComments.toggle()
            handleLikesCommentsToggle()
        }
        
        binding.switchLikesComments.setOnCheckedChangeListener { _, isChecked ->
            handleLikesCommentsToggle()
        }
        
        binding.layoutNewFollowers.setOnClickListener {
            binding.switchNewFollowers.toggle()
            handleNewFollowersToggle()
        }
        
        binding.switchNewFollowers.setOnCheckedChangeListener { _, isChecked ->
            handleNewFollowersToggle()
        }
        
        // Content & Display
        binding.layoutAutoPlay.setOnClickListener {
            binding.switchAutoPlay.toggle()
            handleAutoPlayToggle()
        }
        
        binding.switchAutoPlay.setOnCheckedChangeListener { _, isChecked ->
            handleAutoPlayToggle()
        }
        
        binding.layoutDataSaver.setOnClickListener {
            binding.switchDataSaver.toggle()
            handleDataSaverToggle()
        }
        
        binding.switchDataSaver.setOnCheckedChangeListener { _, isChecked ->
            handleDataSaverToggle()
        }
        
        binding.layoutLanguage.setOnClickListener {
            // Show language selection
            showLanguageOptions()
        }
        
        // Support & About
        binding.layoutHelpCenter.setOnClickListener {
            // Navigate to help center
            // TODO: Implement help center
        }
        
        binding.layoutReportProblem.setOnClickListener {
            // Navigate to report problem
            // TODO: Implement report problem screen
        }
        
        binding.layoutAbout.setOnClickListener {
            // Navigate to about screen
            // TODO: Implement about screen
        }
        
        binding.layoutTerms.setOnClickListener {
            // Navigate to terms of service
            // TODO: Implement terms screen
        }
        
        binding.layoutPrivacyPolicy.setOnClickListener {
            // Navigate to privacy policy
            // TODO: Implement privacy policy screen
        }
        
        // Logout
        binding.layoutLogout.setOnClickListener {
            showLogoutDialog()
        }
        
        binding.layoutDeleteAccount.setOnClickListener {
            showDeleteAccountDialog()
        }
    }
    
    private fun loadSettings() {
        // Load current settings from preferences
        binding.switchPrivateAccount.isChecked = preferenceManager.isPrivateAccount()
        binding.switchPushNotifications.isChecked = preferenceManager.isPushNotificationsEnabled()
        binding.switchLikesComments.isChecked = preferenceManager.isLikesCommentsNotificationEnabled()
        binding.switchNewFollowers.isChecked = preferenceManager.isNewFollowersNotificationEnabled()
        binding.switchAutoPlay.isChecked = preferenceManager.isAutoPlayEnabled()
        binding.switchDataSaver.isChecked = preferenceManager.isDataSaverEnabled()
        
        // Set current language
        binding.tvLanguageValue.text = preferenceManager.getLanguage() ?: "English"
        
        // Set privacy settings
        binding.tvWhoCanMessageValue.text = preferenceManager.getWhoCanMessage() ?: "Everyone"
        binding.tvWhoCanCommentValue.text = preferenceManager.getWhoCanComment() ?: "Everyone"
    }
    
    private fun handlePrivateAccountToggle() {
        val isPrivate = binding.switchPrivateAccount.isChecked
        preferenceManager.setPrivateAccount(isPrivate)
        
        if (isPrivate) {
            showPrivateAccountDialog()
        }
    }
    
    private fun handlePushNotificationsToggle() {
        val isEnabled = binding.switchPushNotifications.isChecked
        preferenceManager.setPushNotificationsEnabled(isEnabled)
        
        // Enable/disable other notification switches based on this
        binding.switchLikesComments.isEnabled = isEnabled
        binding.switchNewFollowers.isEnabled = isEnabled
    }
    
    private fun handleLikesCommentsToggle() {
        val isEnabled = binding.switchLikesComments.isChecked
        preferenceManager.setLikesCommentsNotificationEnabled(isEnabled)
    }
    
    private fun handleNewFollowersToggle() {
        val isEnabled = binding.switchNewFollowers.isChecked
        preferenceManager.setNewFollowersNotificationEnabled(isEnabled)
    }
    
    private fun handleAutoPlayToggle() {
        val isEnabled = binding.switchAutoPlay.isChecked
        preferenceManager.setAutoPlayEnabled(isEnabled)
    }
    
    private fun handleDataSaverToggle() {
        val isEnabled = binding.switchDataSaver.isChecked
        preferenceManager.setDataSaverEnabled(isEnabled)
        
        if (isEnabled) {
            // Automatically disable auto-play when data saver is enabled
            binding.switchAutoPlay.isChecked = false
            preferenceManager.setAutoPlayEnabled(false)
        }
    }
    
    private fun showPrivateAccountDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Private Account")
        builder.setMessage("When your account is private, only people you approve can see your videos and profile information.")
        builder.setPositiveButton("Got it", null)
        builder.show()
    }
    
    private fun showMessagePrivacyOptions() {
        val options = arrayOf("Everyone", "People I follow", "No one")
        val currentSelection = when (preferenceManager.getWhoCanMessage()) {
            "Everyone" -> 0
            "People I follow" -> 1
            "No one" -> 2
            else -> 0
        }
        
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Who can send you direct messages?")
        builder.setSingleChoiceItems(options, currentSelection) { dialog, which ->
            val selection = options[which]
            preferenceManager.setWhoCanMessage(selection)
            binding.tvWhoCanMessageValue.text = selection
            dialog.dismiss()
        }
        builder.show()
    }
    
    private fun showCommentPrivacyOptions() {
        val options = arrayOf("Everyone", "People I follow", "No one")
        val currentSelection = when (preferenceManager.getWhoCanComment()) {
            "Everyone" -> 0
            "People I follow" -> 1
            "No one" -> 2
            else -> 0
        }
        
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Who can comment on your videos?")
        builder.setSingleChoiceItems(options, currentSelection) { dialog, which ->
            val selection = options[which]
            preferenceManager.setWhoCanComment(selection)
            binding.tvWhoCanCommentValue.text = selection
            dialog.dismiss()
        }
        builder.show()
    }
    
    private fun showLanguageOptions() {
        val options = arrayOf("English", "العربية", "Español", "Français", "Deutsch")
        val currentSelection = when (preferenceManager.getLanguage()) {
            "English" -> 0
            "العربية" -> 1
            "Español" -> 2
            "Français" -> 3
            "Deutsch" -> 4
            else -> 0
        }
        
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select Language")
        builder.setSingleChoiceItems(options, currentSelection) { dialog, which ->
            val selection = options[which]
            preferenceManager.setLanguage(selection)
            binding.tvLanguageValue.text = selection
            dialog.dismiss()
            
            // TODO: Apply language change
        }
        builder.show()
    }
    
    private fun showLogoutDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Logout") { _, _ ->
            preferenceManager.clearAll()
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
    
    private fun showDeleteAccountDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to permanently delete your account? This action cannot be undone.")
        builder.setPositiveButton("Delete") { _, _ ->
            showFinalDeleteConfirmation()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
    
    private fun showFinalDeleteConfirmation() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Final Confirmation")
        builder.setMessage("This will permanently delete your account and all your data. Type 'DELETE' to confirm.")
        
        val input = android.widget.EditText(this)
        builder.setView(input)
        
        builder.setPositiveButton("Delete") { _, _ ->
            if (input.text.toString() == "DELETE") {
                // TODO: Implement account deletion
                preferenceManager.clearAll()
                startActivity(Intent(this, AuthActivity::class.java))
                finishAffinity()
            } else {
                android.widget.Toast.makeText(this, "Please type 'DELETE' to confirm", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}