package com.pix.app.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val fullName: String,
    val avatar: String? = null,
    val bio: String? = null,
    val website: String? = null,
    val phoneNumber: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val videosCount: Int = 0,
    val likesCount: Int = 0,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val isActive: Boolean = true,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)