package com.pix.app.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "videos",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val videoUrl: String,
    val thumbnailUrl: String? = null,
    val description: String? = null,
    val duration: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val fileSize: Long = 0,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val viewsCount: Long = 0,
    val downloadCount: Int = 0,
    val isPublic: Boolean = true,
    val allowComments: Boolean = true,
    val allowDuet: Boolean = true,
    val allowStitch: Boolean = true,
    val allowDownload: Boolean = true,
    val musicId: String? = null,
    val effectsUsed: String? = null, // JSON string of effects
    val hashtags: String? = null, // JSON string of hashtags
    val mentions: String? = null, // JSON string of mentioned users
    val location: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)