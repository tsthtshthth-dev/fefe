package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    
    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): VideoEntity?
    
    @Query("SELECT * FROM videos WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getVideosByUser(userId: String): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE isPublic = 1 ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getPublicVideos(limit: Int, offset: Int): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE isPublic = 1 ORDER BY viewsCount DESC LIMIT :limit")
    suspend fun getTrendingVideos(limit: Int = 20): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE isPublic = 1 ORDER BY likesCount DESC LIMIT :limit")
    suspend fun getMostLikedVideos(limit: Int = 20): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE description LIKE '%' || :query || '%' OR hashtags LIKE '%' || :query || '%'")
    suspend fun searchVideos(query: String): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE hashtags LIKE '%' || :hashtag || '%' ORDER BY createdAt DESC")
    suspend fun getVideosByHashtag(hashtag: String): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE userId IN (:userIds) AND isPublic = 1 ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getVideosFromFollowing(userIds: List<String>, limit: Int): List<VideoEntity>
    
    @Query("UPDATE videos SET likesCount = likesCount + :increment WHERE id = :videoId")
    suspend fun updateLikesCount(videoId: String, increment: Int)
    
    @Query("UPDATE videos SET commentsCount = commentsCount + :increment WHERE id = :videoId")
    suspend fun updateCommentsCount(videoId: String, increment: Int)
    
    @Query("UPDATE videos SET sharesCount = sharesCount + :increment WHERE id = :videoId")
    suspend fun updateSharesCount(videoId: String, increment: Int)
    
    @Query("UPDATE videos SET viewsCount = viewsCount + 1 WHERE id = :videoId")
    suspend fun incrementViewCount(videoId: String)
    
    @Query("UPDATE videos SET downloadCount = downloadCount + 1 WHERE id = :videoId")
    suspend fun incrementDownloadCount(videoId: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)
    
    @Update
    suspend fun updateVideo(video: VideoEntity)
    
    @Delete
    suspend fun deleteVideo(video: VideoEntity)
    
    @Query("DELETE FROM videos WHERE id = :videoId")
    suspend fun deleteVideoById(videoId: String)
    
    @Query("DELETE FROM videos WHERE userId = :userId")
    suspend fun deleteVideosByUser(userId: String)
    
    // Flow for real-time updates
    @Query("SELECT * FROM videos WHERE userId = :userId ORDER BY createdAt DESC")
    fun getVideosByUserFlow(userId: String): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE isPublic = 1 ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun getPublicVideosFlow(limit: Int, offset: Int): Flow<List<VideoEntity>>
    
    @Query("SELECT COUNT(*) FROM videos WHERE userId = :userId")
    suspend fun getVideoCountByUser(userId: String): Int
}