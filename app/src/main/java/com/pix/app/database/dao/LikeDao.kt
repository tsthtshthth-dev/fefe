package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.LikeEntity
import com.pix.app.database.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    
    @Query("SELECT * FROM likes WHERE userId = :userId AND videoId = :videoId")
    suspend fun getLike(userId: String, videoId: String): LikeEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM likes WHERE userId = :userId AND videoId = :videoId)")
    suspend fun isLiked(userId: String, videoId: String): Boolean
    
    @Query("""
        SELECT v.* FROM videos v 
        INNER JOIN likes l ON v.id = l.videoId 
        WHERE l.userId = :userId 
        ORDER BY l.createdAt DESC
    """)
    suspend fun getLikedVideos(userId: String): List<VideoEntity>
    
    @Query("SELECT COUNT(*) FROM likes WHERE videoId = :videoId")
    suspend fun getLikesCount(videoId: String): Int
    
    @Query("SELECT COUNT(*) FROM likes WHERE userId = :userId")
    suspend fun getUserLikesCount(userId: String): Int
    
    @Query("""
        SELECT v.* FROM videos v 
        INNER JOIN likes l ON v.id = l.videoId 
        WHERE l.userId = :userId 
        ORDER BY l.createdAt DESC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getLikedVideosPaged(userId: String, limit: Int, offset: Int): List<VideoEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: LikeEntity)
    
    @Delete
    suspend fun deleteLike(like: LikeEntity)
    
    @Query("DELETE FROM likes WHERE userId = :userId AND videoId = :videoId")
    suspend fun unlikeVideo(userId: String, videoId: String)
    
    @Query("DELETE FROM likes WHERE videoId = :videoId")
    suspend fun deleteAllLikesForVideo(videoId: String)
    
    @Query("DELETE FROM likes WHERE userId = :userId")
    suspend fun deleteAllLikesForUser(userId: String)
    
    // Flow for real-time updates
    @Query("SELECT EXISTS(SELECT 1 FROM likes WHERE userId = :userId AND videoId = :videoId)")
    fun isLikedFlow(userId: String, videoId: String): Flow<Boolean>
    
    @Query("""
        SELECT v.* FROM videos v 
        INNER JOIN likes l ON v.id = l.videoId 
        WHERE l.userId = :userId 
        ORDER BY l.createdAt DESC
    """)
    fun getLikedVideosFlow(userId: String): Flow<List<VideoEntity>>
    
    @Query("SELECT COUNT(*) FROM likes WHERE videoId = :videoId")
    fun getLikesCountFlow(videoId: String): Flow<Int>
}