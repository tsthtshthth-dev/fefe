package com.pix.app.database.dao

import androidx.room.*
import com.pix.app.database.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    
    @Query("SELECT * FROM comments WHERE id = :commentId")
    suspend fun getCommentById(commentId: String): CommentEntity?
    
    @Query("SELECT * FROM comments WHERE videoId = :videoId AND parentCommentId IS NULL ORDER BY createdAt DESC")
    suspend fun getCommentsForVideo(videoId: String): List<CommentEntity>
    
    @Query("SELECT * FROM comments WHERE parentCommentId = :parentId ORDER BY createdAt ASC")
    suspend fun getRepliesForComment(parentId: String): List<CommentEntity>
    
    @Query("SELECT * FROM comments WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getCommentsByUser(userId: String): List<CommentEntity>
    
    @Query("SELECT COUNT(*) FROM comments WHERE videoId = :videoId")
    suspend fun getCommentsCount(videoId: String): Int
    
    @Query("SELECT COUNT(*) FROM comments WHERE parentCommentId = :parentId")
    suspend fun getRepliesCount(parentId: String): Int
    
    @Query("UPDATE comments SET likesCount = likesCount + :increment WHERE id = :commentId")
    suspend fun updateLikesCount(commentId: String, increment: Int)
    
    @Query("UPDATE comments SET repliesCount = repliesCount + :increment WHERE id = :commentId")
    suspend fun updateRepliesCount(commentId: String, increment: Int)
    
    @Query("UPDATE comments SET text = :newText, isEdited = 1, updatedAt = :timestamp WHERE id = :commentId")
    suspend fun editComment(commentId: String, newText: String, timestamp: Long)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)
    
    @Update
    suspend fun updateComment(comment: CommentEntity)
    
    @Delete
    suspend fun deleteComment(comment: CommentEntity)
    
    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteCommentById(commentId: String)
    
    @Query("DELETE FROM comments WHERE videoId = :videoId")
    suspend fun deleteCommentsForVideo(videoId: String)
    
    @Query("DELETE FROM comments WHERE userId = :userId")
    suspend fun deleteCommentsForUser(userId: String)
    
    // Flow for real-time updates
    @Query("SELECT * FROM comments WHERE videoId = :videoId AND parentCommentId IS NULL ORDER BY createdAt DESC")
    fun getCommentsForVideoFlow(videoId: String): Flow<List<CommentEntity>>
    
    @Query("SELECT * FROM comments WHERE parentCommentId = :parentId ORDER BY createdAt ASC")
    fun getRepliesForCommentFlow(parentId: String): Flow<List<CommentEntity>>
    
    @Query("SELECT COUNT(*) FROM comments WHERE videoId = :videoId")
    fun getCommentsCountFlow(videoId: String): Flow<Int>
}