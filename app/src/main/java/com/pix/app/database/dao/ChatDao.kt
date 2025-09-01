package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    
    @Query("SELECT * FROM chat_messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?
    
    @Query("""
        SELECT * FROM chat_messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
           OR (senderId = :userId2 AND receiverId = :userId1) 
        ORDER BY createdAt ASC
    """)
    suspend fun getConversation(userId1: String, userId2: String): List<ChatMessageEntity>
    
    @Query("""
        SELECT * FROM chat_messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
           OR (senderId = :userId2 AND receiverId = :userId1) 
        ORDER BY createdAt ASC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getConversationPaged(userId1: String, userId2: String, limit: Int, offset: Int): List<ChatMessageEntity>
    
    @Query("""
        SELECT DISTINCT 
            CASE 
                WHEN senderId = :userId THEN receiverId 
                ELSE senderId 
            END as otherUserId,
            MAX(createdAt) as lastMessageTime
        FROM chat_messages 
        WHERE senderId = :userId OR receiverId = :userId 
        GROUP BY otherUserId 
        ORDER BY lastMessageTime DESC
    """)
    suspend fun getChatList(userId: String): List<Map<String, Any>>
    
    @Query("""
        SELECT COUNT(*) FROM chat_messages 
        WHERE receiverId = :userId AND isRead = 0
    """)
    suspend fun getUnreadMessagesCount(userId: String): Int
    
    @Query("""
        SELECT COUNT(*) FROM chat_messages 
        WHERE senderId = :senderId AND receiverId = :receiverId AND isRead = 0
    """)
    suspend fun getUnreadMessagesCountFromUser(senderId: String, receiverId: String): Int
    
    @Query("""
        SELECT * FROM chat_messages 
        WHERE receiverId = :userId AND isRead = 0 
        ORDER BY createdAt DESC
    """)
    suspend fun getUnreadMessages(userId: String): List<ChatMessageEntity>
    
    @Query("UPDATE chat_messages SET isRead = 1 WHERE senderId = :senderId AND receiverId = :receiverId")
    suspend fun markMessagesAsRead(senderId: String, receiverId: String)
    
    @Query("UPDATE chat_messages SET isDelivered = 1 WHERE receiverId = :receiverId AND isDelivered = 0")
    suspend fun markMessagesAsDelivered(receiverId: String)
    
    @Query("UPDATE chat_messages SET isRead = 1 WHERE id = :messageId")
    suspend fun markMessageAsRead(messageId: String)
    
    @Query("UPDATE chat_messages SET isDelivered = 1 WHERE id = :messageId")
    suspend fun markMessageAsDelivered(messageId: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)
    
    @Update
    suspend fun updateMessage(message: ChatMessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: ChatMessageEntity)
    
    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: String)
    
    @Query("""
        DELETE FROM chat_messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
           OR (senderId = :userId2 AND receiverId = :userId1)
    """)
    suspend fun deleteConversation(userId1: String, userId2: String)
    
    @Query("DELETE FROM chat_messages WHERE senderId = :userId OR receiverId = :userId")
    suspend fun deleteAllMessagesForUser(userId: String)
    
    // Flow for real-time updates
    @Query("""
        SELECT * FROM chat_messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
           OR (senderId = :userId2 AND receiverId = :userId1) 
        ORDER BY createdAt ASC
    """)
    fun getConversationFlow(userId1: String, userId2: String): Flow<List<ChatMessageEntity>>
    
    @Query("""
        SELECT COUNT(*) FROM chat_messages 
        WHERE receiverId = :userId AND isRead = 0
    """)
    fun getUnreadMessagesCountFlow(userId: String): Flow<Int>
    
    @Query("""
        SELECT COUNT(*) FROM chat_messages 
        WHERE senderId = :senderId AND receiverId = :receiverId AND isRead = 0
    """)
    fun getUnreadMessagesCountFromUserFlow(senderId: String, receiverId: String): Flow<Int>
}