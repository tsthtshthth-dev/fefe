package com.pix.app.repository

import android.content.Context
import com.pix.app.database.AppDatabase
import com.pix.app.database.dao.NotificationDao
import com.pix.app.database.entities.NotificationEntity
import com.pix.app.models.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepository(context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val notificationDao: NotificationDao = database.notificationDao()
    
    // Get all notifications for a user
    fun getNotifications(userId: String): Flow<List<Notification>> {
        return notificationDao.getNotificationsByUser(userId).map { entities ->
            entities.map { entity ->
                Notification(
                    id = entity.id,
                    userId = entity.userId,
                    type = entity.type,
                    title = entity.title,
                    message = entity.message,
                    data = entity.data,
                    isRead = entity.isRead,
                    createdAt = entity.createdAt
                )
            }
        }
    }
    
    // Get unread notifications count
    suspend fun getUnreadNotificationsCount(userId: String): Int {
        return notificationDao.getUnreadNotificationsCount(userId)
    }
    
    // Mark notification as read
    suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }
    
    // Mark all notifications as read
    suspend fun markAllAsRead(userId: String) {
        notificationDao.markAllAsRead(userId)
    }
    
    // Delete notification
    suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteNotification(notificationId)
    }
    
    // Clear all notifications
    suspend fun clearAllNotifications(userId: String) {
        notificationDao.clearAllNotifications(userId)
    }
    
    // Create notification
    suspend fun createNotification(notification: Notification) {
        val entity = NotificationEntity(
            id = notification.id,
            userId = notification.userId,
            type = notification.type,
            title = notification.title,
            message = notification.message,
            data = notification.data,
            isRead = notification.isRead,
            createdAt = notification.createdAt
        )
        notificationDao.insertNotification(entity)
    }
    
    // Create like notification
    suspend fun createLikeNotification(
        userId: String,
        likerUserId: String,
        likerUsername: String,
        videoId: String
    ) {
        val notification = Notification(
            id = "like_${System.currentTimeMillis()}",
            userId = userId,
            type = "like",
            title = "إعجاب جديد",
            message = "أعجب $likerUsername بفيديوك",
            data = mapOf(
                "videoId" to videoId,
                "userId" to likerUserId
            ),
            isRead = false,
            createdAt = System.currentTimeMillis()
        )
        createNotification(notification)
    }
    
    // Create comment notification
    suspend fun createCommentNotification(
        userId: String,
        commenterUserId: String,
        commenterUsername: String,
        videoId: String,
        commentText: String
    ) {
        val notification = Notification(
            id = "comment_${System.currentTimeMillis()}",
            userId = userId,
            type = "comment",
            title = "تعليق جديد",
            message = "علق $commenterUsername على فيديوك: \"$commentText\"",
            data = mapOf(
                "videoId" to videoId,
                "userId" to commenterUserId
            ),
            isRead = false,
            createdAt = System.currentTimeMillis()
        )
        createNotification(notification)
    }
    
    // Create follow notification
    suspend fun createFollowNotification(
        userId: String,
        followerUserId: String,
        followerUsername: String
    ) {
        val notification = Notification(
            id = "follow_${System.currentTimeMillis()}",
            userId = userId,
            type = "follow",
            title = "متابع جديد",
            message = "بدأ $followerUsername بمتابعتك",
            data = mapOf(
                "userId" to followerUserId
            ),
            isRead = false,
            createdAt = System.currentTimeMillis()
        )
        createNotification(notification)
    }
    
    // Create mention notification
    suspend fun createMentionNotification(
        userId: String,
        mentionerUserId: String,
        mentionerUsername: String,
        videoId: String
    ) {
        val notification = Notification(
            id = "mention_${System.currentTimeMillis()}",
            userId = userId,
            type = "mention",
            title = "إشارة جديدة",
            message = "أشار إليك $mentionerUsername في فيديو",
            data = mapOf(
                "videoId" to videoId,
                "userId" to mentionerUserId
            ),
            isRead = false,
            createdAt = System.currentTimeMillis()
        )
        createNotification(notification)
    }
    
    // Get notifications by type
    fun getNotificationsByType(userId: String, type: String): Flow<List<Notification>> {
        return notificationDao.getNotificationsByType(userId, type).map { entities ->
            entities.map { entity ->
                Notification(
                    id = entity.id,
                    userId = entity.userId,
                    type = entity.type,
                    title = entity.title,
                    message = entity.message,
                    data = entity.data,
                    isRead = entity.isRead,
                    createdAt = entity.createdAt
                )
            }
        }
    }
    
    // Delete old notifications (older than 30 days)
    suspend fun deleteOldNotifications() {
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
        notificationDao.deleteOldNotifications(thirtyDaysAgo)
    }
}