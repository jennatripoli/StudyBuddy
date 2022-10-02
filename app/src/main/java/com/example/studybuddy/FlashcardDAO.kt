package com.example.studybuddy

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface FlashcardDAO {

    @Insert
    fun insertFlashcard(flashcard : FlashcardEntity)

    @Delete
    fun deleteFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcards WHERE study_set IN (:studySet)")
    fun getFlashcardsFromSet(studySet : String) : List<FlashcardEntity>

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards() : List<FlashcardEntity>

    @Query("DELETE FROM flashcards")
    fun deleteAllFlashcards()

}