package com.example.studybuddy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudySetEntity::class, FlashcardEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao : DAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context): AppDatabase? {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        "study_buddy_database.sqlite").build()
                }
            }
            return INSTANCE
        }
    }
}