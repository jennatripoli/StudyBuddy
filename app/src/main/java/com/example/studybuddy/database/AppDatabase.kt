package com.example.studybuddy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudySetEntity::class, FlashcardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao : DAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context): AppDatabase? {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        "database-name").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}