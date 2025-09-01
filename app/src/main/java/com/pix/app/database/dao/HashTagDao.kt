package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.HashTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HashTagDao {
    
    @Query("SELECT * FROM hashtags WHERE id = :hashtagId")
    suspend fun getHashTagById(hashtagId: String): HashTagEntity?
    
    @Query("SELECT * FROM hashtags WHERE name = :name")
    suspend fun getHashTagByName(name: String): HashTagEntity?
    
    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :query || '%' ORDER BY videosCount DESC")
    suspend fun searchHashTags(query: String): List<HashTagEntity>
    
    @Query("SELECT * FROM hashtags ORDER BY videosCount DESC LIMIT :limit")
    suspend fun getTrendingHashTags(limit: Int = 20): List<HashTagEntity>
    
    @Query("SELECT * FROM hashtags ORDER BY viewsCount DESC LIMIT :limit")
    suspend fun getMostViewedHashTags(limit: Int = 20): List<HashTagEntity>
    
    @Query("SELECT * FROM hashtags WHERE createdAt >= :timestamp ORDER BY videosCount DESC LIMIT :limit")
    suspend fun getNewTrendingHashTags(timestamp: Long, limit: Int = 20): List<HashTagEntity>
    
    @Query("SELECT * FROM hashtags WHERE isBlocked = 0 ORDER BY videosCount DESC")
    suspend fun getAllActiveHashTags(): List<HashTagEntity>
    
    @Query("SELECT * FROM hashtags WHERE isBlocked = 1")
    suspend fun getBlockedHashTags(): List<HashTagEntity>
    
    @Query("UPDATE hashtags SET videosCount = videosCount + :increment WHERE id = :hashtagId")
    suspend fun updateVideosCount(hashtagId: String, increment: Int)
    
    @Query("UPDATE hashtags SET viewsCount = viewsCount + :increment WHERE id = :hashtagId")
    suspend fun updateViewsCount(hashtagId: String, increment: Long)
    
    @Query("UPDATE hashtags SET isBlocked = :isBlocked WHERE id = :hashtagId")
    suspend fun updateBlockedStatus(hashtagId: String, isBlocked: Boolean)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHashTag(hashtag: HashTagEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHashTags(hashtags: List<HashTagEntity>)
    
    @Update
    suspend fun updateHashTag(hashtag: HashTagEntity)
    
    @Delete
    suspend fun deleteHashTag(hashtag: HashTagEntity)
    
    @Query("DELETE FROM hashtags WHERE id = :hashtagId")
    suspend fun deleteHashTagById(hashtagId: String)
    
    @Query("DELETE FROM hashtags WHERE videosCount = 0 AND createdAt < :timestamp")
    suspend fun deleteUnusedHashTags(timestamp: Long)
    
    // Flow for real-time updates
    @Query("SELECT * FROM hashtags ORDER BY videosCount DESC LIMIT :limit")
    fun getTrendingHashTagsFlow(limit: Int = 20): Flow<List<HashTagEntity>>
    
    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :query || '%' ORDER BY videosCount DESC")
    fun searchHashTagsFlow(query: String): Flow<List<HashTagEntity>>
    
    @Query("SELECT * FROM hashtags WHERE isBlocked = 0 ORDER BY videosCount DESC")
    fun getAllActiveHashTagsFlow(): Flow<List<HashTagEntity>>
}