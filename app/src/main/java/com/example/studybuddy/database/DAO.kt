package com.example.studybuddy.database

import androidx.room.*

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudySet(studySet: StudySetEntity)

    @Delete
    fun deleteStudySet(studySet: StudySetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFlashcard(flashcard: FlashcardEntity)

    @Delete
    fun deleteFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards() : List<FlashcardEntity>

    @Query("SELECT * FROM study_sets")
    fun getAllStudySets() : List<StudySetEntity>

    @Transaction
    @Query("SELECT * FROM study_sets WHERE studySetName = :studySetName")
    fun getStudySetWithFlashcards(studySetName : String): List<StudySetWithFlashcards>

}