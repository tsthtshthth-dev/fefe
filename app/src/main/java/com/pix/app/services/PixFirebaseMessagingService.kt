package com.pix.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pix.app.R
import com.pix.app.ui.main.MainActivity
import com.pix.app.ui.notifications.NotificationsActivity
import com.pix.app.utils.PreferenceManager

class PixFirebaseMessagingService : FirebaseMessagingService() {
    
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
        createNotificationChannel()
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // Check if notifications are enabled
        if (!preferenceManager.areNotificationsEnabled()) {
            return
        }
        
        // Handle FCM messages here
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "Pix"
            val body = notification.body ?: ""
            val data = remoteMessage.data
            
            showNotification(title, body, data)
        }
        
        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            handleDataPayload(remoteMessage.data)
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        // Send token to server
        sendTokenToServer(token)
        
        // Store token locally
        preferenceManager.setFCMToken(token)
    }
    
    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create intent based on notification type
        val intent = createNotificationIntent(data)
        val pendingIntent = PendingIntent.getActivity(
            this, 
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Build notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        
        // Add action buttons based on notification type
        addNotificationActions(notificationBuilder, data)
        
        // Show notification
        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
    
    private fun createNotificationIntent(data: Map<String, String>): Intent {
        return when (data["type"]) {
            "like", "comment", "mention" -> {
                // Navigate to specific video
                Intent(this, MainActivity::class.java).apply {
                    putExtra("video_id", data["video_id"])
                    putExtra("open_video", true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
            "follow" -> {
                // Navigate to user profile
                Intent(this, MainActivity::class.java).apply {
                    putExtra("user_id", data["user_id"])
                    putExtra("open_profile", true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
            "message" -> {
                // Navigate to chat
                Intent(this, MainActivity::class.java).apply {
                    putExtra("chat_room_id", data["chat_room_id"])
                    putExtra("open_chat", true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
            else -> {
                // Default to notifications screen
                Intent(this, NotificationsActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
        }
    }
    
    private fun addNotificationActions(
        builder: NotificationCompat.Builder,
        data: Map<String, String>
    ) {
        when (data["type"]) {
            "like" -> {
                // Add like back action
                val likeIntent = Intent(this, NotificationActionReceiver::class.java).apply {
                    action = "ACTION_LIKE_BACK"
                    putExtra("user_id", data["user_id"])
                    putExtra("video_id", data["video_id"])
                }
                val likePendingIntent = PendingIntent.getBroadcast(
                    this,
                    System.currentTimeMillis().toInt(),
                    likeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                builder.addAction(R.drawable.ic_like, "إعجاب", likePendingIntent)
            }
            "comment" -> {
                // Add reply action
                val replyIntent = Intent(this, NotificationActionReceiver::class.java).apply {
                    action = "ACTION_REPLY"
                    putExtra("user_id", data["user_id"])
                    putExtra("video_id", data["video_id"])
                }
                val replyPendingIntent = PendingIntent.getBroadcast(
                    this,
                    System.currentTimeMillis().toInt(),
                    replyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                builder.addAction(R.drawable.ic_comment, "رد", replyPendingIntent)
            }
            "follow" -> {
                // Add follow back action
                val followIntent = Intent(this, NotificationActionReceiver::class.java).apply {
                    action = "ACTION_FOLLOW_BACK"
                    putExtra("user_id", data["user_id"])
                }
                val followPendingIntent = PendingIntent.getBroadcast(
                    this,
                    System.currentTimeMillis().toInt(),
                    followIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                builder.addAction(R.drawable.ic_person_add, "متابعة", followPendingIntent)
            }
        }
    }
    
    private fun handleDataPayload(data: Map<String, String>) {
        // Handle background data processing
        when (data["type"]) {
            "like" -> {
                // Update like count in local database
                // TODO: Implement background like processing
            }
            "comment" -> {
                // Add comment to local database
                // TODO: Implement background comment processing
            }
            "follow" -> {
                // Update follower count
                // TODO: Implement background follow processing
            }
            "message" -> {
                // Add message to local database
                // TODO: Implement background message processing
            }
        }
    }
    
    private fun sendTokenToServer(token: String) {
        // TODO: Send FCM token to your server
        // This should be done via your API
        val userId = preferenceManager.getUserId()
        if (!userId.isNullOrEmpty()) {
            // Make API call to update user's FCM token
            // Example: apiService.updateFCMToken(userId, token)
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pix Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for Pix app"
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "pix_notifications"
    }
}