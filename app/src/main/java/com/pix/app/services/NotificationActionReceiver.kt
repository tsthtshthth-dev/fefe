package com.pix.app.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.pix.app.repository.UserRepository
import com.pix.app.repository.VideoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActionReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val userId = intent.getStringExtra("user_id")
        val videoId = intent.getStringExtra("video_id")
        
        when (action) {
            "ACTION_LIKE_BACK" -> {
                handleLikeBack(context, userId, videoId)
            }
            "ACTION_REPLY" -> {
                handleReply(context, userId, videoId)
            }
            "ACTION_FOLLOW_BACK" -> {
                handleFollowBack(context, userId)
            }
        }
    }
    
    private fun handleLikeBack(context: Context, userId: String?, videoId: String?) {
        if (userId.isNullOrEmpty() || videoId.isNullOrEmpty()) return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val videoRepository = VideoRepository(context)
                videoRepository.likeVideo(videoId, userId)
                
                // Show success message on main thread
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "تم الإعجاب بالفيديو", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "فشل في الإعجاب", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun handleReply(context: Context, userId: String?, videoId: String?) {
        if (userId.isNullOrEmpty() || videoId.isNullOrEmpty()) return
        
        // Open the app to reply to comment
        val intent = Intent(context, com.pix.app.ui.main.MainActivity::class.java).apply {
            putExtra("video_id", videoId)
            putExtra("open_video", true)
            putExtra("open_comments", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
    
    private fun handleFollowBack(context: Context, userId: String?) {
        if (userId.isNullOrEmpty()) return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository(context)
                userRepository.followUser(userId)
                
                // Show success message on main thread
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "تمت المتابعة", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "فشل في المتابعة", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}