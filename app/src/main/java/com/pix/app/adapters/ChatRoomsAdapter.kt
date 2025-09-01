package com.pix.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pix.app.R
import com.pix.app.databinding.ItemChatRoomBinding
import com.pix.app.models.ChatRoom
import com.pix.app.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomsAdapter(
    private var chatRooms: List<ChatRoom>,
    private val onChatClick: (ChatRoom) -> Unit
) : RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding: ItemChatRoomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat_room,
            parent,
            false
        )
        return ChatRoomViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(chatRooms[position])
    }
    
    override fun getItemCount(): Int = chatRooms.size
    
    fun updateChats(newChats: List<ChatRoom>) {
        chatRooms = newChats
        notifyDataSetChanged()
    }
    
    inner class ChatRoomViewHolder(private val binding: ItemChatRoomBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(chatRoom: ChatRoom) {
            binding.chatRoom = chatRoom
            
            // Get other participant (not current user)
            val otherParticipant = chatRoom.participants.find { it != "current_user" } ?: "Unknown"
            
            // Set participant info (in real app, fetch user data)
            binding.tvUsername.text = "@$otherParticipant"
            binding.tvFullName.text = otherParticipant.replaceFirstChar { it.uppercase() }
            
            // Load participant avatar (placeholder for now)
            Glide.with(binding.root.context)
                .load("") // Would load actual user avatar
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.ivUserAvatar)
            
            // Set last message
            chatRoom.lastMessage?.let { message ->
                when (message.messageType) {
                    MessageType.TEXT -> {
                        binding.tvLastMessage.text = message.message
                        binding.ivMessageType.visibility = View.GONE
                    }
                    MessageType.IMAGE -> {
                        binding.tvLastMessage.text = "📷 Photo"
                        binding.ivMessageType.visibility = View.VISIBLE
                        binding.ivMessageType.setImageResource(R.drawable.ic_image)
                    }
                    MessageType.VIDEO -> {
                        binding.tvLastMessage.text = "🎥 Video"
                        binding.ivMessageType.visibility = View.VISIBLE
                        binding.ivMessageType.setImageResource(R.drawable.ic_video)
                    }
                    MessageType.AUDIO -> {
                        binding.tvLastMessage.text = "🎵 Voice message"
                        binding.ivMessageType.visibility = View.VISIBLE
                        binding.ivMessageType.setImageResource(R.drawable.ic_audio)
                    }
                }
                
                // Set timestamp
                binding.tvTimestamp.text = getTimeAgo(message.createdAt)
                
                // Show read status for sent messages
                if (message.senderId == "current_user") {
                    binding.ivReadStatus.visibility = View.VISIBLE
                    binding.ivReadStatus.setImageResource(
                        if (message.isRead) R.drawable.ic_read else R.drawable.ic_sent
                    )
                } else {
                    binding.ivReadStatus.visibility = View.GONE
                }
            }
            
            // Show unread count
            if (chatRoom.unreadCount > 0) {
                binding.tvUnreadCount.visibility = View.VISIBLE
                binding.tvUnreadCount.text = if (chatRoom.unreadCount > 99) "99+" else chatRoom.unreadCount.toString()
            } else {
                binding.tvUnreadCount.visibility = View.GONE
            }
            
            // Set online status (placeholder)
            binding.viewOnlineStatus.visibility = View.VISIBLE // Would check actual online status
            
            binding.root.setOnClickListener {
                onChatClick(chatRoom)
            }
            
            binding.executePendingBindings()
        }
        
        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 60000 -> "الآن"
                diff < 3600000 -> "${diff / 60000}د"
                diff < 86400000 -> "${diff / 3600000}س"
                diff < 604800000 -> "${diff / 86400000}ي"
                else -> {
                    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }
}