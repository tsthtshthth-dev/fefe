package com.pix.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatar: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val caption: String,
    var likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    var isLiked: Boolean,
    var isFollowing: Boolean,
    val createdAt: Long,
    val duration: Int = 0, // in seconds
    val hashtags: List<String> = emptyList(),
    val mentions: List<String> = emptyList(),
    val musicId: String? = null,
    val musicTitle: String? = null,
    val musicArtist: String? = null
) : Parcelable

@Parcelize
data class User(
    val id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val avatar: String,
    val bio: String,
    val website: String,
    val followersCount: Int,
    val followingCount: Int,
    val videosCount: Int,
    val likesCount: Int,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val createdAt: Long
) : Parcelable

@Parcelize
data class Comment(
    val id: String,
    val videoId: String,
    val userId: String,
    val username: String,
    val userAvatar: String,
    val text: String,
    val likesCount: Int,
    val isLiked: Boolean,
    val createdAt: Long,
    val replies: List<Comment> = emptyList()
) : Parcelable

@Parcelize
data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val messageType: MessageType,
    val mediaUrl: String? = null,
    val thumbnailUrl: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long
) : Parcelable

enum class MessageType {
    TEXT, IMAGE, VIDEO, AUDIO
}

@Parcelize
data class ChatRoom(
    val id: String,
    val participants: List<String>,
    val lastMessage: ChatMessage?,
    val unreadCount: Int,
    val updatedAt: Long
) : Parcelable

@Parcelize
data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val data: Map<String, String> = emptyMap(),
    val isRead: Boolean = false,
    val createdAt: Long
) : Parcelable

enum class NotificationType {
    LIKE, COMMENT, FOLLOW, MENTION, MESSAGE, SYSTEM
}

@Parcelize
data class HashTag(
    val name: String,
    val videosCount: Int,
    val viewsCount: Long,
    val isFollowing: Boolean = false
) : Parcelable

@Parcelize
data class Music(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Int,
    val audioUrl: String,
    val coverUrl: String,
    val videosCount: Int
) : Parcelable