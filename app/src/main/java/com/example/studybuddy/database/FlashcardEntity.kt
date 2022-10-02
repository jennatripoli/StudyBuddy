package com.example.studybuddy.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity (
        @PrimaryKey(autoGenerate = false)
        val studySetName: String,
        val term: String,
        val definition: String
        )