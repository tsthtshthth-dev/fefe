package com.pix.app.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hashtags")
data class HashTagEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val videosCount: Int = 0,
    val viewsCount: Long = 0,
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)