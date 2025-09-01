package com.pix.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pix.app.R
import com.pix.app.adapters.ProfileVideosAdapter
import com.pix.app.databinding.FragmentProfileBinding
import com.pix.app.models.Video
import com.pix.app.ui.auth.AuthActivity
import com.pix.app.ui.settings.SettingsActivity
import com.pix.app.utils.PreferenceManager

class ProfileFragment : Fragment() {
    
    private lateinit var binding: FragmentProfileBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var videosAdapter: ProfileVideosAdapter
    private val userVideos = mutableListOf<Video>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferenceManager = PreferenceManager(requireContext())
        
        setupUI()
        setupRecyclerView()
        setupClickListeners()
        loadUserData()
        loadUserVideos()
    }
    
    private fun setupUI() {
        // Load user profile data
        binding.tvUsername.text = "@${preferenceManager.getUsername() ?: "username"}"
        binding.tvFullName.text = preferenceManager.getFullName() ?: "Full Name"
        binding.tvBio.text = preferenceManager.getBio() ?: "Bio not set"
        
        // Load profile image
        val profileImage = preferenceManager.getProfileImage()
        if (!profileImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImage)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.ivProfileImage)
        }
        
        // Load stats
        binding.tvFollowersCount.text = formatCount(preferenceManager.getFollowersCount())
        binding.tvFollowingCount.text = formatCount(preferenceManager.getFollowingCount())
        binding.tvVideosCount.text = formatCount(preferenceManager.getVideosCount())
    }
    
    private fun setupRecyclerView() {
        videosAdapter = ProfileVideosAdapter(userVideos) { video ->
            // Handle video click - play video
            // TODO: Implement video playback
        }
        
        binding.recyclerViewVideos.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = videosAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            // Navigate to edit profile
            // TODO: Implement edit profile screen
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        
        binding.btnMenu.setOnClickListener {
            // Show profile menu
            showProfileMenu()
        }
        
        binding.layoutFollowers.setOnClickListener {
            // Navigate to followers list
            // TODO: Implement followers screen
        }
        
        binding.layoutFollowing.setOnClickListener {
            // Navigate to following list
            // TODO: Implement following screen
        }
        
        binding.layoutVideos.setOnClickListener {
            // Already showing videos
        }
        
        // Tab selection
        binding.btnTabVideos.setOnClickListener {
            selectTab("videos")
        }
        
        binding.btnTabLiked.setOnClickListener {
            selectTab("liked")
        }
        
        binding.btnTabSaved.setOnClickListener {
            selectTab("saved")
        }
    }
    
    private fun loadUserData() {
        // In a real app, this would fetch from API
        // For now, we'll use sample data
        preferenceManager.setFollowersCount(1250)
        preferenceManager.setFollowingCount(890)
        preferenceManager.setVideosCount(45)
        
        // Update UI
        binding.tvFollowersCount.text = formatCount(1250)
        binding.tvFollowingCount.text = formatCount(890)
        binding.tvVideosCount.text = formatCount(45)
    }
    
    private fun loadUserVideos() {
        // Sample user videos
        val sampleVideos = listOf(
            Video(
                id = "user_v1",
                userId = preferenceManager.getUserId() ?: "",
                username = preferenceManager.getUsername() ?: "",
                userAvatar = preferenceManager.getProfileImage() ?: "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "My first video! 🎉",
                likesCount = 1250,
                commentsCount = 89,
                sharesCount = 45,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            ),
            Video(
                id = "user_v2",
                userId = preferenceManager.getUserId() ?: "",
                username = preferenceManager.getUsername() ?: "",
                userAvatar = preferenceManager.getProfileImage() ?: "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "Having fun with friends! 😄",
                likesCount = 890,
                commentsCount = 67,
                sharesCount = 23,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis() - 86400000
            ),
            Video(
                id = "user_v3",
                userId = preferenceManager.getUserId() ?: "",
                username = preferenceManager.getUsername() ?: "",
                userAvatar = preferenceManager.getProfileImage() ?: "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "Beautiful sunset today! 🌅",
                likesCount = 2340,
                commentsCount = 156,
                sharesCount = 78,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis() - 172800000
            )
        )
        
        userVideos.clear()
        userVideos.addAll(sampleVideos)
        videosAdapter.notifyDataSetChanged()
    }
    
    private fun selectTab(tab: String) {
        // Reset all tabs
        binding.btnTabVideos.setTextColor(resources.getColor(R.color.text_secondary, null))
        binding.btnTabLiked.setTextColor(resources.getColor(R.color.text_secondary, null))
        binding.btnTabSaved.setTextColor(resources.getColor(R.color.text_secondary, null))
        
        binding.viewTabVideos.visibility = View.INVISIBLE
        binding.viewTabLiked.visibility = View.INVISIBLE
        binding.viewTabSaved.visibility = View.INVISIBLE
        
        // Select current tab
        when (tab) {
            "videos" -> {
                binding.btnTabVideos.setTextColor(resources.getColor(R.color.white, null))
                binding.viewTabVideos.visibility = View.VISIBLE
                loadUserVideos()
            }
            "liked" -> {
                binding.btnTabLiked.setTextColor(resources.getColor(R.color.white, null))
                binding.viewTabLiked.visibility = View.VISIBLE
                loadLikedVideos()
            }
            "saved" -> {
                binding.btnTabSaved.setTextColor(resources.getColor(R.color.white, null))
                binding.viewTabSaved.visibility = View.VISIBLE
                loadSavedVideos()
            }
        }
    }
    
    private fun loadLikedVideos() {
        // Load liked videos (sample data)
        userVideos.clear()
        // Add sample liked videos
        videosAdapter.notifyDataSetChanged()
    }
    
    private fun loadSavedVideos() {
        // Load saved videos (sample data)
        userVideos.clear()
        // Add sample saved videos
        videosAdapter.notifyDataSetChanged()
    }
    
    private fun showProfileMenu() {
        // Show bottom sheet or popup menu
        val options = arrayOf(
            "Share Profile",
            "Copy Profile Link",
            "QR Code",
            "Settings",
            "Logout"
        )
        
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> shareProfile()
                1 -> copyProfileLink()
                2 -> showQRCode()
                3 -> startActivity(Intent(requireContext(), SettingsActivity::class.java))
                4 -> logout()
            }
        }
        builder.show()
    }
    
    private fun shareProfile() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out my Pix profile: @${preferenceManager.getUsername()}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share Profile"))
    }
    
    private fun copyProfileLink() {
        val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Profile Link", "@${preferenceManager.getUsername()}")
        clipboard.setPrimaryClip(clip)
        
        android.widget.Toast.makeText(requireContext(), "Profile link copied!", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    private fun showQRCode() {
        // TODO: Implement QR code generation and display
        android.widget.Toast.makeText(requireContext(), "QR Code feature coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    private fun logout() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Logout") { _, _ ->
            preferenceManager.clearAll()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
    
    private fun formatCount(count: Int): String {
        return when {
            count >= 1000000 -> "${count / 1000000}M"
            count >= 1000 -> "${count / 1000}K"
            else -> count.toString()
        }
    }
}