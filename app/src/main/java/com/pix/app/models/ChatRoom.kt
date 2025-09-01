package com.pix.app.models

data class ChatRoom(
    val id: String,
    val participants: List<String>,
    val lastMessage: ChatMessage?,
    val unreadCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)