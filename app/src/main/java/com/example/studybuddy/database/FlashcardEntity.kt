package com.example.studybuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
class FlashcardEntity (
        @PrimaryKey(autoGenerate = true)
        var id : Long = 0L,
        val studySetName : String = "",
        val term : String = "",
        val definition : String =""
        )