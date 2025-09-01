package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE username LIKE '%' || :query || '%' OR fullName LIKE '%' || :query || '%'")
    suspend fun searchUsers(query: String): List<UserEntity>
    
    @Query("SELECT * FROM users WHERE isVerified = 1 ORDER BY followersCount DESC LIMIT :limit")
    suspend fun getVerifiedUsers(limit: Int = 20): List<UserEntity>
    
    @Query("SELECT * FROM users ORDER BY followersCount DESC LIMIT :limit")
    suspend fun getPopularUsers(limit: Int = 20): List<UserEntity>
    
    @Query("SELECT * FROM users WHERE createdAt >= :timestamp ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getNewUsers(timestamp: Long, limit: Int = 20): List<UserEntity>
    
    @Query("SELECT COUNT(*) FROM users WHERE id = :userId")
    suspend fun userExists(userId: String): Int
    
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun usernameExists(username: String): Int
    
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int
    
    @Query("UPDATE users SET followersCount = followersCount + :increment WHERE id = :userId")
    suspend fun updateFollowersCount(userId: String, increment: Int)
    
    @Query("UPDATE users SET followingCount = followingCount + :increment WHERE id = :userId")
    suspend fun updateFollowingCount(userId: String, increment: Int)
    
    @Query("UPDATE users SET videosCount = videosCount + :increment WHERE id = :userId")
    suspend fun updateVideosCount(userId: String, increment: Int)
    
    @Query("UPDATE users SET likesCount = likesCount + :increment WHERE id = :userId")
    suspend fun updateLikesCount(userId: String, increment: Int)
    
    @Query("UPDATE users SET lastSeen = :timestamp WHERE id = :userId")
    suspend fun updateLastSeen(userId: String, timestamp: Long)
    
    @Query("UPDATE users SET isActive = :isActive WHERE id = :userId")
    suspend fun updateActiveStatus(userId: String, isActive: Boolean)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: String)
    
    // Flow for real-time updates
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE username LIKE '%' || :query || '%' OR fullName LIKE '%' || :query || '%'")
    fun searchUsersFlow(query: String): Flow<List<UserEntity>>
}