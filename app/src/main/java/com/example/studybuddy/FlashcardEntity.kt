package com.example.studybuddy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
class FlashcardEntity (
    @PrimaryKey val key: Int,
    @ColumnInfo(name = "term") val term: String?,
    @ColumnInfo(name = "definition") val definition: String?
    )