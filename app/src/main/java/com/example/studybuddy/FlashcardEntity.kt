package com.example.studybuddy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "study_set") val studySet: String?,
    @ColumnInfo(name = "term") val term: String?,
    @ColumnInfo(name = "definition") val definition: String?
    )