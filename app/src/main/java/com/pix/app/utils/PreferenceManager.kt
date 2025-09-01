package com.pix.app.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREF_NAME = "PixPreferences"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_FULL_NAME = "fullName"
        private const val KEY_PROFILE_IMAGE = "profileImage"
        private const val KEY_BIO = "bio"
        private const val KEY_FOLLOWERS_COUNT = "followersCount"
        private const val KEY_FOLLOWING_COUNT = "followingCount"
        private const val KEY_VIDEOS_COUNT = "videosCount"
        private const val KEY_LIKES_COUNT = "likesCount"
        private const val KEY_FIRST_TIME = "firstTime"
        private const val KEY_NOTIFICATIONS_ENABLED = "notificationsEnabled"
        private const val KEY_DARK_MODE = "darkMode"
        
        // Privacy Keys
        private const val KEY_PRIVATE_ACCOUNT = "privateAccount"
        private const val KEY_WHO_CAN_MESSAGE = "whoCanMessage"
        private const val KEY_WHO_CAN_COMMENT = "whoCanComment"
        
        // Notification Keys
        private const val KEY_PUSH_NOTIFICATIONS = "pushNotifications"
        private const val KEY_LIKES_COMMENTS_NOTIFICATION = "likesCommentsNotification"
        private const val KEY_NEW_FOLLOWERS_NOTIFICATION = "newFollowersNotification"
        
        // App Settings Keys
        private const val KEY_AUTO_PLAY = "autoPlay"
        private const val KEY_DATA_SAVER = "dataSaver"
        private const val KEY_LANGUAGE = "language"
        
        // Device Keys
        private const val KEY_DEVICE_TOKEN = "deviceToken"
        private const val KEY_FCM_TOKEN = "fcmToken"
        private const val KEY_LAST_SYNC_TIME = "lastSyncTime"
    }
    
    // Login status
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
    
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // User data
    fun setUserData(userId: String, username: String, email: String, fullName: String) {
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putString(KEY_FULL_NAME, fullName)
            apply()
        }
    }
    
    fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)
    fun getUsername(): String? = sharedPreferences.getString(KEY_USERNAME, null)
    fun getEmail(): String? = sharedPreferences.getString(KEY_EMAIL, null)
    fun getFullName(): String? = sharedPreferences.getString(KEY_FULL_NAME, null)
    
    // Profile data
    fun setProfileImage(imageUrl: String) {
        sharedPreferences.edit().putString(KEY_PROFILE_IMAGE, imageUrl).apply()
    }
    
    fun getProfileImage(): String? = sharedPreferences.getString(KEY_PROFILE_IMAGE, null)
    
    fun setBio(bio: String) {
        sharedPreferences.edit().putString(KEY_BIO, bio).apply()
    }
    
    fun getBio(): String? = sharedPreferences.getString(KEY_BIO, null)
    
    // Stats
    fun setFollowersCount(count: Int) {
        sharedPreferences.edit().putInt(KEY_FOLLOWERS_COUNT, count).apply()
    }
    
    fun getFollowersCount(): Int = sharedPreferences.getInt(KEY_FOLLOWERS_COUNT, 0)
    
    fun setFollowingCount(count: Int) {
        sharedPreferences.edit().putInt(KEY_FOLLOWING_COUNT, count).apply()
    }
    
    fun getFollowingCount(): Int = sharedPreferences.getInt(KEY_FOLLOWING_COUNT, 0)
    
    fun setVideosCount(count: Int) {
        sharedPreferences.edit().putInt(KEY_VIDEOS_COUNT, count).apply()
    }
    
    fun getVideosCount(): Int = sharedPreferences.getInt(KEY_VIDEOS_COUNT, 0)
    
    // App settings
    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_TIME, isFirstTime).apply()
    }
    
    fun isFirstTime(): Boolean = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)
    
    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }
    
    fun areNotificationsEnabled(): Boolean = sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    
    fun setDarkMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }
    
    fun isDarkMode(): Boolean = sharedPreferences.getBoolean(KEY_DARK_MODE, true)
    
    // Clear all data (logout)
    // User Statistics
    fun getFollowersCount(): Int = sharedPreferences.getInt(KEY_FOLLOWERS_COUNT, 0)
    fun setFollowersCount(count: Int) = sharedPreferences.edit().putInt(KEY_FOLLOWERS_COUNT, count).apply()
    
    fun getFollowingCount(): Int = sharedPreferences.getInt(KEY_FOLLOWING_COUNT, 0)
    fun setFollowingCount(count: Int) = sharedPreferences.edit().putInt(KEY_FOLLOWING_COUNT, count).apply()
    
    fun getVideosCount(): Int = sharedPreferences.getInt(KEY_VIDEOS_COUNT, 0)
    fun setVideosCount(count: Int) = sharedPreferences.edit().putInt(KEY_VIDEOS_COUNT, count).apply()
    
    fun getLikesCount(): Int = sharedPreferences.getInt(KEY_LIKES_COUNT, 0)
    fun setLikesCount(count: Int) = sharedPreferences.edit().putInt(KEY_LIKES_COUNT, count).apply()
    
    // Privacy Settings
    fun isPrivateAccount(): Boolean = sharedPreferences.getBoolean(KEY_PRIVATE_ACCOUNT, false)
    fun setPrivateAccount(isPrivate: Boolean) = sharedPreferences.edit().putBoolean(KEY_PRIVATE_ACCOUNT, isPrivate).apply()
    
    fun getWhoCanMessage(): String? = sharedPreferences.getString(KEY_WHO_CAN_MESSAGE, "Everyone")
    fun setWhoCanMessage(setting: String) = sharedPreferences.edit().putString(KEY_WHO_CAN_MESSAGE, setting).apply()
    
    fun getWhoCanComment(): String? = sharedPreferences.getString(KEY_WHO_CAN_COMMENT, "Everyone")
    fun setWhoCanComment(setting: String) = sharedPreferences.edit().putString(KEY_WHO_CAN_COMMENT, setting).apply()
    
    // Notification Settings
    fun isPushNotificationsEnabled(): Boolean = sharedPreferences.getBoolean(KEY_PUSH_NOTIFICATIONS, true)
    fun setPushNotificationsEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(KEY_PUSH_NOTIFICATIONS, enabled).apply()
    
    fun isLikesCommentsNotificationEnabled(): Boolean = sharedPreferences.getBoolean(KEY_LIKES_COMMENTS_NOTIFICATION, true)
    fun setLikesCommentsNotificationEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(KEY_LIKES_COMMENTS_NOTIFICATION, enabled).apply()
    
    fun isNewFollowersNotificationEnabled(): Boolean = sharedPreferences.getBoolean(KEY_NEW_FOLLOWERS_NOTIFICATION, true)
    fun setNewFollowersNotificationEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(KEY_NEW_FOLLOWERS_NOTIFICATION, enabled).apply()
    
    // App Settings
    fun isAutoPlayEnabled(): Boolean = sharedPreferences.getBoolean(KEY_AUTO_PLAY, true)
    fun setAutoPlayEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(KEY_AUTO_PLAY, enabled).apply()
    
    fun isDataSaverEnabled(): Boolean = sharedPreferences.getBoolean(KEY_DATA_SAVER, false)
    fun setDataSaverEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean(KEY_DATA_SAVER, enabled).apply()
    
    fun getLanguage(): String? = sharedPreferences.getString(KEY_LANGUAGE, "English")
    fun setLanguage(language: String) = sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
    
    // Device Settings
    fun getDeviceToken(): String? = sharedPreferences.getString(KEY_DEVICE_TOKEN, null)
    fun setDeviceToken(token: String) = sharedPreferences.edit().putString(KEY_DEVICE_TOKEN, token).apply()
    
    fun getFCMToken(): String? = sharedPreferences.getString(KEY_FCM_TOKEN, null)
    fun setFCMToken(token: String) = sharedPreferences.edit().putString(KEY_FCM_TOKEN, token).apply()
    
    fun getLastSyncTime(): Long = sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0)
    fun setLastSyncTime(time: Long) = sharedPreferences.edit().putLong(KEY_LAST_SYNC_TIME, time).apply()
    
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}