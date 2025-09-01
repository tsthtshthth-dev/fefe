package com.pix.app.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.pix.app.database.dao.*
import com.pix.app.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        VideoEntity::class,
        CommentEntity::class,
        ChatMessageEntity::class,
        NotificationEntity::class,
        FollowEntity::class,
        LikeEntity::class,
        HashTagEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao
    abstract fun chatDao(): ChatDao
    abstract fun notificationDao(): NotificationDao
    abstract fun followDao(): FollowDao
    abstract fun likeDao(): LikeDao
    abstract fun hashTagDao(): HashTagDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pix_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}