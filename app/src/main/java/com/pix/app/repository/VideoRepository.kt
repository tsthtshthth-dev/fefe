package com.pix.app.repository

import android.content.Context
import com.pix.app.database.AppDatabase
import com.pix.app.database.dao.VideoDao
import com.pix.app.database.dao.UserDao
import com.pix.app.database.entities.VideoEntity
import com.pix.app.models.Video
import com.pix.app.utils.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class VideoRepository(context: Context) {
    
    private val videoDao: VideoDao = AppDatabase.getDatabase(context).videoDao()
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
    private val preferenceManager = PreferenceManager(context)
    
    // Video Upload and Management
    suspend fun uploadVideo(
        videoUrl: String,
        thumbnailUrl: String? = null,
        description: String? = null,
        duration: Long = 0,
        width: Int = 0,
        height: Int = 0,
        fileSize: Long = 0,
        isPublic: Boolean = true,
        allowComments: Boolean = true,
        allowDuet: Boolean = true,
        allowStitch: Boolean = true,
        allowDownload: Boolean = true,
        musicId: String? = null,
        effectsUsed: String? = null,
        hashtags: String? = null,
        mentions: String? = null,
        location: String? = null
    ): Result<VideoEntity> {
        return try {
            val userId = preferenceManager.getUserId()
                ?: return Result.failure(Exception("User not logged in"))
            
            val videoId = UUID.randomUUID().toString()
            val video = VideoEntity(
                id = videoId,
                userId = userId,
                videoUrl = videoUrl,
                thumbnailUrl = thumbnailUrl,
                description = description,
                duration = duration,
                width = width,
                height = height,
                fileSize = fileSize,
                isPublic = isPublic,
                allowComments = allowComments,
                allowDuet = allowDuet,
                allowStitch = allowStitch,
                allowDownload = allowDownload,
                musicId = musicId,
                effectsUsed = effectsUsed,
                hashtags = hashtags,
                mentions = mentions,
                location = location
            )
            
            videoDao.insertVideo(video)
            
            // Update user's video count
            userDao.updateVideosCount(userId, 1)
            
            Result.success(video)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateVideo(
        videoId: String,
        description: String? = null,
        isPublic: Boolean? = null,
        allowComments: Boolean? = null,
        allowDuet: Boolean? = null,
        allowStitch: Boolean? = null,
        allowDownload: Boolean? = null
    ): Result<VideoEntity> {
        return try {
            val currentVideo = videoDao.getVideoById(videoId)
                ?: return Result.failure(Exception("Video not found"))
            
            val userId = preferenceManager.getUserId()
            if (currentVideo.userId != userId) {
                return Result.failure(Exception("Unauthorized"))
            }
            
            val updatedVideo = currentVideo.copy(
                description = description ?: currentVideo.description,
                isPublic = isPublic ?: currentVideo.isPublic,
                allowComments = allowComments ?: currentVideo.allowComments,
                allowDuet = allowDuet ?: currentVideo.allowDuet,
                allowStitch = allowStitch ?: currentVideo.allowStitch,
                allowDownload = allowDownload ?: currentVideo.allowDownload,
                updatedAt = System.currentTimeMillis()
            )
            
            videoDao.updateVideo(updatedVideo)
            Result.success(updatedVideo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteVideo(videoId: String): Result<Boolean> {
        return try {
            val video = videoDao.getVideoById(videoId)
                ?: return Result.failure(Exception("Video not found"))
            
            val userId = preferenceManager.getUserId()
            if (video.userId != userId) {
                return Result.failure(Exception("Unauthorized"))
            }
            
            videoDao.deleteVideoById(videoId)
            
            // Update user's video count
            userDao.updateVideosCount(userId, -1)
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Video Discovery
    suspend fun getPublicVideos(limit: Int = 20, offset: Int = 0): List<Video> {
        return try {
            val videos = videoDao.getPublicVideos(limit, offset)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getPublicVideosFlow(limit: Int = 20, offset: Int = 0): Flow<List<Video>> {
        return videoDao.getPublicVideosFlow(limit, offset).map { videos ->
            videos.map { it.toVideo() }
        }
    }
    
    suspend fun getTrendingVideos(limit: Int = 20): List<Video> {
        return try {
            val videos = videoDao.getTrendingVideos(limit)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getMostLikedVideos(limit: Int = 20): List<Video> {
        return try {
            val videos = videoDao.getMostLikedVideos(limit)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun searchVideos(query: String): List<Video> {
        return try {
            val videos = videoDao.searchVideos(query)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getVideosByHashtag(hashtag: String): List<Video> {
        return try {
            val videos = videoDao.getVideosByHashtag(hashtag)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getVideosByUser(userId: String): List<Video> {
        return try {
            val videos = videoDao.getVideosByUser(userId)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getVideosByUserFlow(userId: String): Flow<List<Video>> {
        return videoDao.getVideosByUserFlow(userId).map { videos ->
            videos.map { it.toVideo() }
        }
    }
    
    suspend fun getCurrentUserVideos(): List<Video> {
        val userId = preferenceManager.getUserId() ?: return emptyList()
        return getVideosByUser(userId)
    }
    
    fun getCurrentUserVideosFlow(): Flow<List<Video>> {
        val userId = preferenceManager.getUserId() ?: return kotlinx.coroutines.flow.flowOf(emptyList())
        return getVideosByUserFlow(userId)
    }
    
    suspend fun getVideosFromFollowing(userIds: List<String>, limit: Int = 20): List<Video> {
        return try {
            val videos = videoDao.getVideosFromFollowing(userIds, limit)
            videos.map { it.toVideo() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Video Interactions
    suspend fun incrementViewCount(videoId: String) {
        try {
            videoDao.incrementViewCount(videoId)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun incrementDownloadCount(videoId: String) {
        try {
            videoDao.incrementDownloadCount(videoId)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun updateLikesCount(videoId: String, increment: Int) {
        try {
            videoDao.updateLikesCount(videoId, increment)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun updateCommentsCount(videoId: String, increment: Int) {
        try {
            videoDao.updateCommentsCount(videoId, increment)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun updateSharesCount(videoId: String, increment: Int) {
        try {
            videoDao.updateSharesCount(videoId, increment)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun getVideoById(videoId: String): Video? {
        return try {
            videoDao.getVideoById(videoId)?.toVideo()
        } catch (e: Exception) {
            null
        }
    }
    
    // Extension function to convert VideoEntity to Video model
    private suspend fun VideoEntity.toVideo(): Video {
        val user = userDao.getUserById(this.userId)
        return Video(
            id = this.id,
            userId = this.userId,
            username = user?.username ?: "",
            userAvatar = user?.avatar ?: "",
            videoUrl = this.videoUrl,
            thumbnailUrl = this.thumbnailUrl ?: "",
            caption = this.description ?: "",
            likesCount = this.likesCount,
            commentsCount = this.commentsCount,
            sharesCount = this.sharesCount,
            isLiked = false, // This should be determined by checking LikeEntity
            isFollowing = false, // This should be determined by checking FollowEntity
            createdAt = this.createdAt
        )
    }
}