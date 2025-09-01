package com.pix.app.ui.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.pix.app.R
import com.pix.app.databinding.ActivityVideoUploadBinding
import com.pix.app.models.Video
import com.pix.app.repository.VideoRepository
import com.pix.app.utils.PreferenceManager
import kotlinx.coroutines.launch
import java.util.*

class VideoUploadActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityVideoUploadBinding
    private lateinit var videoRepository: VideoRepository
    private lateinit var preferenceManager: PreferenceManager
    private var videoUri: Uri? = null
    private var exoPlayer: ExoPlayer? = null
    private var isUploading = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        videoRepository = VideoRepository(this)
        preferenceManager = PreferenceManager(this)
        
        // Get video URI from intent
        videoUri = intent.getParcelableExtra("video_uri")
        
        setupUI()
        setupClickListeners()
        loadVideoPreview()
    }
    
    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Set user info
        binding.textViewUsername.text = "@${preferenceManager.getUsername()}"
        val profileImage = preferenceManager.getProfileImage()
        if (!profileImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImage)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.imageViewProfile)
        }
    }
    
    private fun setupClickListeners() {
        binding.buttonPost.setOnClickListener {
            if (!isUploading) {
                uploadVideo()
            }
        }
        
        binding.buttonDraft.setOnClickListener {
            saveDraft()
        }
        
        binding.switchAllowComments.setOnCheckedChangeListener { _, isChecked ->
            // Handle comments setting
        }
        
        binding.switchAllowDuet.setOnCheckedChangeListener { _, isChecked ->
            // Handle duet setting
        }
        
        binding.switchAllowStitch.setOnCheckedChangeListener { _, isChecked ->
            // Handle stitch setting
        }
        
        // Privacy settings
        binding.radioPublic.setOnClickListener {
            updatePrivacySetting("public")
        }
        
        binding.radioFriends.setOnClickListener {
            updatePrivacySetting("friends")
        }
        
        binding.radioPrivate.setOnClickListener {
            updatePrivacySetting("private")
        }
    }
    
    private fun loadVideoPreview() {
        videoUri?.let { uri ->
            // Initialize ExoPlayer
            exoPlayer = ExoPlayer.Builder(this).build()
            binding.playerView.player = exoPlayer
            
            // Set media item
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.playWhenReady = true
            
            // Load thumbnail
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_video)
                .into(binding.imageViewThumbnail)
        }
    }
    
    private fun uploadVideo() {
        val description = binding.editTextDescription.text.toString().trim()
        val hashtags = extractHashtags(description)
        
        if (videoUri == null) {
            Toast.makeText(this, "لا يوجد فيديو للرفع", Toast.LENGTH_SHORT).show()
            return
        }
        
        isUploading = true
        updateUploadingState(true)
        
        lifecycleScope.launch {
            try {
                val video = Video(
                    id = UUID.randomUUID().toString(),
                    userId = preferenceManager.getUserId() ?: "",
                    username = preferenceManager.getUsername() ?: "",
                    userAvatar = preferenceManager.getProfileImage() ?: "",
                    videoUrl = videoUri.toString(), // In real app, upload to server first
                    thumbnailUrl = "", // Generate thumbnail
                    description = description,
                    hashtags = hashtags,
                    musicName = "", // TODO: Add music selection
                    musicUrl = "",
                    likesCount = 0,
                    commentsCount = 0,
                    sharesCount = 0,
                    viewsCount = 0,
                    isLiked = false,
                    isFollowing = false,
                    createdAt = System.currentTimeMillis(),
                    privacy = getSelectedPrivacy(),
                    allowComments = binding.switchAllowComments.isChecked,
                    allowDuet = binding.switchAllowDuet.isChecked,
                    allowStitch = binding.switchAllowStitch.isChecked
                )
                
                // Save video to database
                videoRepository.createVideo(video)
                
                // Show success message
                Toast.makeText(this@VideoUploadActivity, "تم رفع الفيديو بنجاح", Toast.LENGTH_SHORT).show()
                
                // Navigate back to main activity
                val intent = Intent(this@VideoUploadActivity, com.pix.app.ui.main.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                
            } catch (e: Exception) {
                Toast.makeText(this@VideoUploadActivity, "فشل في رفع الفيديو: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isUploading = false
                updateUploadingState(false)
            }
        }
    }
    
    private fun saveDraft() {
        // TODO: Implement save draft functionality
        Toast.makeText(this, "تم حفظ المسودة", Toast.LENGTH_SHORT).show()
        finish()
    }
    
    private fun updateUploadingState(uploading: Boolean) {
        binding.buttonPost.isEnabled = !uploading
        binding.buttonDraft.isEnabled = !uploading
        binding.progressBarUpload.visibility = if (uploading) View.VISIBLE else View.GONE
        
        if (uploading) {
            binding.buttonPost.text = "جاري الرفع..."
        } else {
            binding.buttonPost.text = "نشر"
        }
    }
    
    private fun updatePrivacySetting(privacy: String) {
        when (privacy) {
            "public" -> {
                binding.radioPublic.isChecked = true
                binding.radioFriends.isChecked = false
                binding.radioPrivate.isChecked = false
            }
            "friends" -> {
                binding.radioPublic.isChecked = false
                binding.radioFriends.isChecked = true
                binding.radioPrivate.isChecked = false
            }
            "private" -> {
                binding.radioPublic.isChecked = false
                binding.radioFriends.isChecked = false
                binding.radioPrivate.isChecked = true
            }
        }
    }
    
    private fun getSelectedPrivacy(): String {
        return when {
            binding.radioFriends.isChecked -> "friends"
            binding.radioPrivate.isChecked -> "private"
            else -> "public"
        }
    }
    
    private fun extractHashtags(text: String): List<String> {
        val hashtagPattern = "#\\w+".toRegex()
        return hashtagPattern.findAll(text).map { it.value }.toList()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
    
    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }
    
    override fun onResume() {
        super.onResume()
        exoPlayer?.play()
    }
}