package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?
    
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getNotificationsForUser(userId: String): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0 ORDER BY createdAt DESC")
    suspend fun getUnreadNotifications(userId: String): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE userId = :userId AND type = :type ORDER BY createdAt DESC")
    suspend fun getNotificationsByType(userId: String, type: String): List<NotificationEntity>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    suspend fun getUnreadNotificationsCount(userId: String): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId")
    suspend fun getTotalNotificationsCount(userId: String): Int
    
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)
    
    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)
    
    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId AND type = :type")
    suspend fun markAllAsReadByType(userId: String, type: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotificationById(notificationId: String)
    
    @Query("DELETE FROM notifications WHERE userId = :userId")
    suspend fun deleteAllNotificationsForUser(userId: String)
    
    @Query("DELETE FROM notifications WHERE userId = :userId AND type = :type")
    suspend fun deleteNotificationsByType(userId: String, type: String)
    
    @Query("DELETE FROM notifications WHERE createdAt < :timestamp")
    suspend fun deleteOldNotifications(timestamp: Long)
    
    // Flow for real-time updates
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getNotificationsForUserFlow(userId: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotificationsFlow(userId: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadNotificationsCountFlow(userId: String): Flow<Int>
    
    @Query("SELECT * FROM notifications WHERE userId = :userId AND type = :type ORDER BY createdAt DESC")
    fun getNotificationsByTypeFlow(userId: String, type: String): Flow<List<NotificationEntity>>
}