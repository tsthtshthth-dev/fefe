package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.FollowEntity
import com.pix.app.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowDao {
    
    @Query("SELECT * FROM follows WHERE followerId = :userId AND followingId = :targetUserId")
    suspend fun getFollow(userId: String, targetUserId: String): FollowEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :userId AND followingId = :targetUserId)")
    suspend fun isFollowing(userId: String, targetUserId: String): Boolean
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f ON u.id = f.followingId 
        WHERE f.followerId = :userId 
        ORDER BY f.createdAt DESC
    """)
    suspend fun getFollowing(userId: String): List<UserEntity>
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f ON u.id = f.followerId 
        WHERE f.followingId = :userId 
        ORDER BY f.createdAt DESC
    """)
    suspend fun getFollowers(userId: String): List<UserEntity>
    
    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :userId")
    suspend fun getFollowingCount(userId: String): Int
    
    @Query("SELECT COUNT(*) FROM follows WHERE followingId = :userId")
    suspend fun getFollowersCount(userId: String): Int
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f1 ON u.id = f1.followingId 
        INNER JOIN follows f2 ON u.id = f2.followerId 
        WHERE f1.followerId = :userId AND f2.followingId = :userId
    """)
    suspend fun getMutualFollows(userId: String): List<UserEntity>
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f1 ON u.id = f1.followerId 
        INNER JOIN follows f2 ON f1.followingId = f2.followingId 
        WHERE f2.followerId = :userId AND u.id != :userId
        GROUP BY u.id 
        ORDER BY COUNT(*) DESC 
        LIMIT :limit
    """)
    suspend fun getSuggestedUsers(userId: String, limit: Int = 20): List<UserEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)
    
    @Delete
    suspend fun deleteFollow(follow: FollowEntity)
    
    @Query("DELETE FROM follows WHERE followerId = :userId AND followingId = :targetUserId")
    suspend fun unfollow(userId: String, targetUserId: String)
    
    @Query("DELETE FROM follows WHERE followerId = :userId")
    suspend fun deleteAllFollowing(userId: String)
    
    @Query("DELETE FROM follows WHERE followingId = :userId")
    suspend fun deleteAllFollowers(userId: String)
    
    // Flow for real-time updates
    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :userId AND followingId = :targetUserId)")
    fun isFollowingFlow(userId: String, targetUserId: String): Flow<Boolean>
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f ON u.id = f.followingId 
        WHERE f.followerId = :userId 
        ORDER BY f.createdAt DESC
    """)
    fun getFollowingFlow(userId: String): Flow<List<UserEntity>>
    
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN follows f ON u.id = f.followerId 
        WHERE f.followingId = :userId 
        ORDER BY f.createdAt DESC
    """)
    fun getFollowersFlow(userId: String): Flow<List<UserEntity>>
    
    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :userId")
    fun getFollowingCountFlow(userId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM follows WHERE followingId = :userId")
    fun getFollowersCountFlow(userId: String): Flow<Int>
}