package com.example.studybuddy.database

import androidx.room.*

@Dao
interface DAO {

    @Insert
    suspend fun insertStudySet(studySet: StudySetEntity)

    @Query("DELETE FROM STUDY_SETS WHERE studySetName = :name")
    suspend fun deleteStudySet(name : String)

    @Update
    suspend fun updateStudySet(studySet: StudySetEntity)

    @Insert
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Query("DELETE FROM flashcards WHERE studySetName = :setName")
    suspend fun deleteAllFlashcardsFromSet(setName: String)

    @Query("SELECT * FROM flashcards WHERE studySetName = :setName")
    suspend fun getFlashcardsForStudySet(setName : String) : List<FlashcardEntity>

    @Query("SELECT * FROM flashcards")
    suspend fun getAllFlashcards() : List<FlashcardEntity>

    @Query("SELECT * FROM study_sets")
    suspend fun getAllStudySets() : List<StudySetEntity>

    @Query("SELECT * FROM study_sets ORDER BY id DESC LIMIT 1")
    suspend fun getMostRecentStudySet() : StudySetEntity?

    @Query("SELECT * FROM flashcards ORDER BY id DESC LIMIT 1")
    suspend fun getMostRecentFlashcard() : FlashcardEntity?

}