package com.example.studybuddy.database

import androidx.room.Embedded
import androidx.room.Relation

data class StudySetWithFlashcards(
    @Embedded val studySet: StudySetEntity,
    @Relation(
        parentColumn = "studySetName",
        entityColumn = "studySetName"
    )
    val flashcards: List<FlashcardEntity>
)