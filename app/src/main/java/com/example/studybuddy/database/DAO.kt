package com.example.studybuddy.database

import androidx.room.*

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySet(studySet: StudySetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Transaction
    @Query("SELECT * FROM study_sets WHERE studySetName = :studySetName")
    suspend fun getStudySetWithFlashcards(studySetName : String): List<StudySetWithFlashcards>

}