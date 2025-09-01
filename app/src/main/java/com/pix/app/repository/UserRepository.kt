package com.pix.app.repository

import android.content.Context
import com.pix.app.database.AppDatabase
import com.pix.app.database.dao.UserDao
import com.pix.app.database.entities.UserEntity
import com.pix.app.models.User
import com.pix.app.utils.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class UserRepository(context: Context) {
    
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
    private val preferenceManager = PreferenceManager(context)
    
    // User Authentication
    suspend fun registerUser(
        username: String,
        email: String,
        fullName: String,
        password: String
    ): Result<UserEntity> {
        return try {
            // Check if username or email already exists
            if (userDao.usernameExists(username) > 0) {
                return Result.failure(Exception("Username already exists"))
            }
            
            if (userDao.emailExists(email) > 0) {
                return Result.failure(Exception("Email already exists"))
            }
            
            val userId = UUID.randomUUID().toString()
            val user = UserEntity(
                id = userId,
                username = username,
                email = email,
                fullName = fullName
            )
            
            userDao.insertUser(user)
            
            // Save user session
            preferenceManager.setUserId(userId)
            preferenceManager.setUsername(username)
            preferenceManager.setEmail(email)
            preferenceManager.setFullName(fullName)
            preferenceManager.setLoggedIn(true)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginUser(usernameOrEmail: String, password: String): Result<UserEntity> {
        return try {
            val user = if (usernameOrEmail.contains("@")) {
                userDao.getUserByEmail(usernameOrEmail)
            } else {
                userDao.getUserByUsername(usernameOrEmail)
            }
            
            if (user != null) {
                // Save user session
                preferenceManager.setUserId(user.id)
                preferenceManager.setUsername(user.username)
                preferenceManager.setEmail(user.email)
                preferenceManager.setFullName(user.fullName)
                preferenceManager.setProfileImage(user.avatar)
                preferenceManager.setBio(user.bio)
                preferenceManager.setLoggedIn(true)
                
                // Update last seen
                userDao.updateLastSeen(user.id, System.currentTimeMillis())
                
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun logoutUser() {
        preferenceManager.clearAll()
    }
    
    // User Profile Management
    suspend fun getCurrentUser(): UserEntity? {
        val userId = preferenceManager.getUserId()
        return userId?.let { userDao.getUserById(it) }
    }
    
    fun getCurrentUserFlow(): Flow<UserEntity?> {
        val userId = preferenceManager.getUserId()
        return if (userId != null) {
            userDao.getUserByIdFlow(userId)
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }
    
    suspend fun updateUserProfile(
        fullName: String? = null,
        bio: String? = null,
        website: String? = null,
        avatar: String? = null,
        phoneNumber: String? = null,
        dateOfBirth: String? = null,
        gender: String? = null,
        location: String? = null
    ): Result<UserEntity> {
        return try {
            val userId = preferenceManager.getUserId()
                ?: return Result.failure(Exception("User not logged in"))
            
            val currentUser = userDao.getUserById(userId)
                ?: return Result.failure(Exception("User not found"))
            
            val updatedUser = currentUser.copy(
                fullName = fullName ?: currentUser.fullName,
                bio = bio ?: currentUser.bio,
                website = website ?: currentUser.website,
                avatar = avatar ?: currentUser.avatar,
                phoneNumber = phoneNumber ?: currentUser.phoneNumber,
                dateOfBirth = dateOfBirth ?: currentUser.dateOfBirth,
                gender = gender ?: currentUser.gender,
                location = location ?: currentUser.location,
                updatedAt = System.currentTimeMillis()
            )
            
            userDao.updateUser(updatedUser)
            
            // Update preferences
            fullName?.let { preferenceManager.setFullName(it) }
            bio?.let { preferenceManager.setBio(it) }
            avatar?.let { preferenceManager.setProfileImage(it) }
            
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updatePrivacySettings(isPrivate: Boolean): Result<Boolean> {
        return try {
            val userId = preferenceManager.getUserId()
                ?: return Result.failure(Exception("User not logged in"))
            
            val currentUser = userDao.getUserById(userId)
                ?: return Result.failure(Exception("User not found"))
            
            val updatedUser = currentUser.copy(
                isPrivate = isPrivate,
                updatedAt = System.currentTimeMillis()
            )
            
            userDao.updateUser(updatedUser)
            preferenceManager.setPrivateAccount(isPrivate)
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // User Discovery
    suspend fun searchUsers(query: String): List<User> {
        return try {
            val users = userDao.searchUsers(query)
            users.map { it.toUser() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun searchUsersFlow(query: String): Flow<List<User>> {
        return userDao.searchUsersFlow(query).map { users ->
            users.map { it.toUser() }
        }
    }
    
    suspend fun getVerifiedUsers(limit: Int = 20): List<User> {
        return try {
            val users = userDao.getVerifiedUsers(limit)
            users.map { it.toUser() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getPopularUsers(limit: Int = 20): List<User> {
        return try {
            val users = userDao.getPopularUsers(limit)
            users.map { it.toUser() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getUserById(userId: String): User? {
        return try {
            userDao.getUserById(userId)?.toUser()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getUserByUsername(username: String): User? {
        return try {
            userDao.getUserByUsername(username)?.toUser()
        } catch (e: Exception) {
            null
        }
    }
    
    // User Statistics
    suspend fun updateUserStats(
        videosCountIncrement: Int = 0,
        followersCountIncrement: Int = 0,
        followingCountIncrement: Int = 0,
        likesCountIncrement: Int = 0
    ) {
        try {
            val userId = preferenceManager.getUserId() ?: return
            
            if (videosCountIncrement != 0) {
                userDao.updateVideosCount(userId, videosCountIncrement)
            }
            if (followersCountIncrement != 0) {
                userDao.updateFollowersCount(userId, followersCountIncrement)
            }
            if (followingCountIncrement != 0) {
                userDao.updateFollowingCount(userId, followingCountIncrement)
            }
            if (likesCountIncrement != 0) {
                userDao.updateLikesCount(userId, likesCountIncrement)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun deleteUser(): Result<Boolean> {
        return try {
            val userId = preferenceManager.getUserId()
                ?: return Result.failure(Exception("User not logged in"))
            
            userDao.deleteUserById(userId)
            preferenceManager.clearAll()
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Extension function to convert UserEntity to User model
    private fun UserEntity.toUser(): User {
        return User(
            id = this.id,
            username = this.username,
            fullName = this.fullName,
            email = this.email,
            avatar = this.avatar ?: "",
            bio = this.bio ?: "",
            website = this.website ?: "",
            followersCount = this.followersCount,
            followingCount = this.followingCount,
            videosCount = this.videosCount,
            likesCount = this.likesCount,
            isVerified = this.isVerified,
            createdAt = this.createdAt
        )
    }
}