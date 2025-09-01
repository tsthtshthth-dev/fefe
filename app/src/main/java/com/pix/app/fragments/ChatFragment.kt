package com.pix.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pix.app.R
import com.pix.app.adapters.ChatRoomsAdapter
import com.pix.app.databinding.FragmentChatBinding
import com.pix.app.models.ChatMessage
import com.pix.app.models.ChatRoom
import com.pix.app.models.MessageType

class ChatFragment : Fragment() {
    
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter
    private val chatRooms = mutableListOf<ChatRoom>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        loadChatRooms()
    }
    
    private fun setupRecyclerView() {
        chatRoomsAdapter = ChatRoomsAdapter(chatRooms) { chatRoom ->
            // Navigate to chat conversation
            // TODO: Implement chat conversation screen
        }
        
        binding.recyclerViewChats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatRoomsAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnNewMessage.setOnClickListener {
            // Open new message screen
            // TODO: Implement new message screen
        }
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchChats(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadChatRooms()
                } else {
                    searchChats(newText)
                }
                return true
            }
        })
    }
    
    private fun loadChatRooms() {
        // Sample chat rooms
        val sampleChatRooms = listOf(
            ChatRoom(
                id = "chat1",
                participants = listOf("current_user", "user1"),
                lastMessage = ChatMessage(
                    id = "msg1",
                    senderId = "user1",
                    receiverId = "current_user",
                    message = "Hey! How are you doing?",
                    messageType = MessageType.TEXT,
                    createdAt = System.currentTimeMillis() - 300000 // 5 minutes ago
                ),
                unreadCount = 2,
                updatedAt = System.currentTimeMillis() - 300000
            ),
            ChatRoom(
                id = "chat2",
                participants = listOf("current_user", "user2"),
                lastMessage = ChatMessage(
                    id = "msg2",
                    senderId = "current_user",
                    receiverId = "user2",
                    message = "Thanks for the video! 😄",
                    messageType = MessageType.TEXT,
                    isRead = true,
                    createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
                ),
                unreadCount = 0,
                updatedAt = System.currentTimeMillis() - 3600000
            ),
            ChatRoom(
                id = "chat3",
                participants = listOf("current_user", "user3"),
                lastMessage = ChatMessage(
                    id = "msg3",
                    senderId = "user3",
                    receiverId = "current_user",
                    message = "Check out this amazing sunset! 🌅",
                    messageType = MessageType.IMAGE,
                    mediaUrl = "https://example.com/sunset.jpg",
                    createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
                ),
                unreadCount = 1,
                updatedAt = System.currentTimeMillis() - 7200000
            ),
            ChatRoom(
                id = "chat4",
                participants = listOf("current_user", "user4"),
                lastMessage = ChatMessage(
                    id = "msg4",
                    senderId = "user4",
                    receiverId = "current_user",
                    message = "Voice message",
                    messageType = MessageType.AUDIO,
                    mediaUrl = "https://example.com/voice.mp3",
                    createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
                ),
                unreadCount = 0,
                updatedAt = System.currentTimeMillis() - 86400000
            ),
            ChatRoom(
                id = "chat5",
                participants = listOf("current_user", "user5"),
                lastMessage = ChatMessage(
                    id = "msg5",
                    senderId = "current_user",
                    receiverId = "user5",
                    message = "Great dance moves! 💃",
                    messageType = MessageType.TEXT,
                    isRead = true,
                    createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
                ),
                unreadCount = 0,
                updatedAt = System.currentTimeMillis() - 172800000
            )
        )
        
        chatRooms.clear()
        chatRooms.addAll(sampleChatRooms)
        chatRoomsAdapter.notifyDataSetChanged()
        
        // Update UI based on chat rooms count
        if (chatRooms.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.recyclerViewChats.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.recyclerViewChats.visibility = View.VISIBLE
        }
    }
    
    private fun searchChats(query: String) {
        // Filter chat rooms based on search query
        // In a real app, this would search through participant names and message content
        val filteredChats = chatRooms.filter { chatRoom ->
            // For demo purposes, we'll just filter by participant IDs
            chatRoom.participants.any { it.contains(query, ignoreCase = true) } ||
            chatRoom.lastMessage?.message?.contains(query, ignoreCase = true) == true
        }
        
        chatRoomsAdapter.updateChats(filteredChats)
    }
}