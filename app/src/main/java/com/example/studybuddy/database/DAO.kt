package com.example.studybuddy.database

import androidx.room.*

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySet(studySet: StudySetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcards")
    suspend fun getAllFlashcards() : List<FlashcardEntity>

    @Query("SELECT * FROM study_sets")
    suspend fun getAllStudySets() : List<StudySetEntity>

    @Transaction
    @Query("SELECT * FROM study_sets WHERE studySetName = :studySetName")
    suspend fun getStudySetWithFlashcards(studySetName : String): List<StudySetWithFlashcards>

}