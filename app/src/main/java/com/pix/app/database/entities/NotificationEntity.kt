package com.pix.app.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["fromUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val userId: String, // User who receives the notification
    val fromUserId: String? = null, // User who triggered the notification
    val type: String, // like, comment, follow, mention, etc.
    val title: String,
    val message: String,
    val data: String? = null, // JSON data for additional info
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)