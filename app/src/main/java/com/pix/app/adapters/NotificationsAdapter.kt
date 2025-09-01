package com.pix.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pix.app.databinding.ItemNotificationBinding
import com.pix.app.models.Notification
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
    
    private val notifications = mutableListOf<Notification>()
    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    fun updateNotifications(newNotifications: List<Notification>) {
        notifications.clear()
        notifications.addAll(newNotifications)
        notifyDataSetChanged()
    }
    
    fun updateNotification(notification: Notification) {
        val index = notifications.indexOfFirst { it.id == notification.id }
        if (index != -1) {
            notifications[index] = notification
            notifyItemChanged(index)
        }
    }
    
    fun clearAll() {
        notifications.clear()
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }
    
    override fun getItemCount(): Int = notifications.size
    
    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(notification: Notification) {
            binding.apply {
                // Set notification icon based on type
                val iconRes = when (notification.type) {
                    "like" -> com.pix.app.R.drawable.ic_like
                    "comment" -> com.pix.app.R.drawable.ic_comment
                    "follow" -> com.pix.app.R.drawable.ic_person_add
                    "mention" -> com.pix.app.R.drawable.ic_at
                    else -> com.pix.app.R.drawable.ic_notifications
                }
                imageViewIcon.setImageResource(iconRes)
                
                // Set notification content
                textViewTitle.text = notification.title
                textViewMessage.text = notification.message
                textViewTime.text = formatTime(notification.createdAt)
                
                // Set read/unread state
                val alpha = if (notification.isRead) 0.6f else 1.0f
                root.alpha = alpha
                
                // Show unread indicator
                viewUnreadIndicator.visibility = if (notification.isRead) {
                    android.view.View.GONE
                } else {
                    android.view.View.VISIBLE
                }
                
                // Set click listener
                root.setOnClickListener {
                    onNotificationClick(notification)
                }
            }
        }
        
        private fun formatTime(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 60000 -> "الآن" // Less than 1 minute
                diff < 3600000 -> "${diff / 60000}د" // Less than 1 hour
                diff < 86400000 -> "${diff / 3600000}س" // Less than 1 day
                diff < 604800000 -> "${diff / 86400000}ي" // Less than 1 week
                else -> dateFormat.format(Date(timestamp))
            }
        }
    }
}