package com.pix.app.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pix.app.R
import com.pix.app.adapters.NotificationsAdapter
import com.pix.app.databinding.ActivityNotificationsBinding
import com.pix.app.models.Notification
import com.pix.app.utils.PreferenceManager
import kotlinx.coroutines.launch

class NotificationsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var adapter: NotificationsAdapter
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        setupUI()
        loadNotifications()
    }
    
    private fun setupUI() {
        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Setup RecyclerView
        adapter = NotificationsAdapter { notification ->
            handleNotificationClick(notification)
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = this@NotificationsActivity.adapter
        }
        
        // Setup refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadNotifications()
        }
        
        // Setup clear all button
        binding.buttonClearAll.setOnClickListener {
            clearAllNotifications()
        }
    }
    
    private fun loadNotifications() {
        binding.swipeRefreshLayout.isRefreshing = true
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewEmpty.visibility = View.GONE
        
        lifecycleScope.launch {
            try {
                // Mock notifications - replace with actual implementation
                val notifications = getMockNotifications()
                
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                
                if (notifications.isEmpty()) {
                    binding.textViewEmpty.visibility = View.VISIBLE
                    binding.buttonClearAll.visibility = View.GONE
                } else {
                    binding.textViewEmpty.visibility = View.GONE
                    binding.buttonClearAll.visibility = View.VISIBLE
                }
                
                adapter.updateNotifications(notifications)
                
            } catch (e: Exception) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.textViewEmpty.text = getString(R.string.loading_error)
            }
        }
    }
    
    private fun handleNotificationClick(notification: Notification) {
        // Mark as read
        markNotificationAsRead(notification)
        
        // Handle different notification types
        when (notification.type) {
            "like" -> {
                // Navigate to video
                // TODO: Navigate to specific video
            }
            "comment" -> {
                // Navigate to video comments
                // TODO: Navigate to video with comments
            }
            "follow" -> {
                // Navigate to user profile
                // TODO: Navigate to user profile
            }
            "mention" -> {
                // Navigate to video where user was mentioned
                // TODO: Navigate to specific video
            }
            else -> {
                // Handle other notification types
            }
        }
    }
    
    private fun markNotificationAsRead(notification: Notification) {
        // TODO: Implement mark as read functionality
        lifecycleScope.launch {
            try {
                // Update notification in database
                // notificationRepository.markAsRead(notification.id)
                
                // Update UI
                val updatedNotification = notification.copy(isRead = true)
                adapter.updateNotification(updatedNotification)
                
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    private fun clearAllNotifications() {
        lifecycleScope.launch {
            try {
                // TODO: Implement clear all functionality
                // notificationRepository.clearAll()
                
                // Update UI
                adapter.clearAll()
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.buttonClearAll.visibility = View.GONE
                
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    private suspend fun getMockNotifications(): List<Notification> {
        // Mock notifications - replace with actual implementation
        return listOf(
            Notification(
                id = "1",
                userId = preferenceManager.getUserId() ?: "",
                type = "like",
                title = "إعجاب جديد",
                message = "أعجب أحمد بفيديوك",
                data = mapOf("videoId" to "video1", "userId" to "user1"),
                isRead = false,
                createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
            ),
            Notification(
                id = "2",
                userId = preferenceManager.getUserId() ?: "",
                type = "comment",
                title = "تعليق جديد",
                message = "علقت سارة على فيديوك: 'رائع!'",
                data = mapOf("videoId" to "video2", "userId" to "user2"),
                isRead = false,
                createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
            ),
            Notification(
                id = "3",
                userId = preferenceManager.getUserId() ?: "",
                type = "follow",
                title = "متابع جديد",
                message = "بدأ محمد بمتابعتك",
                data = mapOf("userId" to "user3"),
                isRead = true,
                createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
            ),
            Notification(
                id = "4",
                userId = preferenceManager.getUserId() ?: "",
                type = "mention",
                title = "إشارة جديدة",
                message = "أشار إليك علي في فيديو",
                data = mapOf("videoId" to "video3", "userId" to "user4"),
                isRead = true,
                createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
            ),
            Notification(
                id = "5",
                userId = preferenceManager.getUserId() ?: "",
                type = "like",
                title = "إعجاب جديد",
                message = "أعجبت فاطمة بفيديوك",
                data = mapOf("videoId" to "video4", "userId" to "user5"),
                isRead = true,
                createdAt = System.currentTimeMillis() - 259200000 // 3 days ago
            )
        )
    }
}