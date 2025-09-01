package com.pix.app.repository

import android.content.Context
import com.pix.app.database.AppDatabase
import com.pix.app.database.dao.ChatMessageDao
import com.pix.app.database.entities.ChatMessageEntity
import com.pix.app.models.ChatMessage
import com.pix.app.models.ChatRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository(context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val chatMessageDao: ChatMessageDao = database.chatMessageDao()
    
    // Get all chat rooms for a user
    suspend fun getChatRooms(userId: String): List<ChatRoom> {
        // Mock implementation - replace with actual database queries
        return listOf(
            ChatRoom(
                id = "room1",
                name = "أحمد محمد",
                lastMessage = "مرحبا، كيف حالك؟",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2,
                avatar = "",
                isOnline = true,
                participants = listOf(userId, "user1")
            ),
            ChatRoom(
                id = "room2",
                name = "سارة أحمد",
                lastMessage = "شكراً لك على الفيديو الرائع",
                lastMessageTime = System.currentTimeMillis() - 7200000,
                unreadCount = 0,
                avatar = "",
                isOnline = false,
                participants = listOf(userId, "user2")
            ),
            ChatRoom(
                id = "room3",
                name = "محمد علي",
                lastMessage = "هل يمكننا التعاون؟",
                lastMessageTime = System.currentTimeMillis() - 86400000,
                unreadCount = 1,
                avatar = "",
                isOnline = true,
                participants = listOf(userId, "user3")
            )
        )
    }
    
    // Get messages for a chat room
    fun getMessages(chatRoomId: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesByChatRoom(chatRoomId).map { entities ->
            entities.map { entity ->
                ChatMessage(
                    id = entity.id,
                    chatRoomId = entity.chatRoomId,
                    senderId = entity.senderId,
                    senderName = entity.senderName,
                    senderAvatar = entity.senderAvatar,
                    message = entity.message,
                    messageType = entity.messageType,
                    timestamp = entity.timestamp,
                    isRead = entity.isRead,
                    replyToMessageId = entity.replyToMessageId
                )
            }
        }
    }
    
    // Send a message
    suspend fun sendMessage(message: ChatMessage) {
        val entity = ChatMessageEntity(
            id = message.id,
            chatRoomId = message.chatRoomId,
            senderId = message.senderId,
            senderName = message.senderName,
            senderAvatar = message.senderAvatar,
            message = message.message,
            messageType = message.messageType,
            timestamp = message.timestamp,
            isRead = message.isRead,
            replyToMessageId = message.replyToMessageId
        )
        chatMessageDao.insertMessage(entity)
    }
    
    // Mark messages as read
    suspend fun markMessagesAsRead(chatRoomId: String, userId: String) {
        chatMessageDao.markMessagesAsRead(chatRoomId, userId)
    }
    
    // Delete a message
    suspend fun deleteMessage(messageId: String) {
        chatMessageDao.deleteMessage(messageId)
    }
    
    // Search messages
    suspend fun searchMessages(query: String, chatRoomId: String? = null): List<ChatMessage> {
        val entities = if (chatRoomId != null) {
            chatMessageDao.searchMessagesInRoom(query, chatRoomId)
        } else {
            chatMessageDao.searchMessages(query)
        }
        
        return entities.map { entity ->
            ChatMessage(
                id = entity.id,
                chatRoomId = entity.chatRoomId,
                senderId = entity.senderId,
                senderName = entity.senderName,
                senderAvatar = entity.senderAvatar,
                message = entity.message,
                messageType = entity.messageType,
                timestamp = entity.timestamp,
                isRead = entity.isRead,
                replyToMessageId = entity.replyToMessageId
            )
        }
    }
    
    // Get unread messages count
    suspend fun getUnreadMessagesCount(userId: String): Int {
        return chatMessageDao.getUnreadMessagesCount(userId)
    }
    
    // Create a new chat room
    suspend fun createChatRoom(participants: List<String>): String {
        // Mock implementation - replace with actual logic
        val roomId = "room_${System.currentTimeMillis()}"
        // TODO: Create chat room in database
        return roomId
    }
    
    // Block/Unblock user
    suspend fun blockUser(userId: String, blockedUserId: String) {
        // TODO: Implement block functionality
    }
    
    suspend fun unblockUser(userId: String, blockedUserId: String) {
        // TODO: Implement unblock functionality
    }
    
    // Report message
    suspend fun reportMessage(messageId: String, reason: String) {
        // TODO: Implement report functionality
    }
}