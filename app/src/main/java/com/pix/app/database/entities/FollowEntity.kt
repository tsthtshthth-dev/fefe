package com.pix.app.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "follows",
    primaryKeys = ["followerId", "followingId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["followerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["followingId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["followerId"]),
        Index(value = ["followingId"])
    ]
)
data class FollowEntity(
    val followerId: String,
    val followingId: String,
    val createdAt: Long = System.currentTimeMillis()
)