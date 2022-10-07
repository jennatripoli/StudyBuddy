package com.example.studybuddy.database

import androidx.room.*

@Dao
interface DAO {

    @Insert
    suspend fun insertStudySet(studySet: StudySetEntity)

//    @Delete
//    suspend fun deleteStudySet(studySet: StudySetEntity)

    @Query("DELETE FROM STUDY_SETS WHERE studySetName = :name")
    suspend fun deleteStudySet(name : String)

    @Update
    suspend fun updateStudySet(studySet: StudySetEntity)

    @Insert
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcards")
    suspend fun getAllFlashcards() : List<FlashcardEntity>

    @Query("SELECT * FROM flashcards WHERE studySetName = :setName")
    suspend fun getFlashcardsForStudySet(setName : String) : List<FlashcardEntity>

    @Query("SELECT * FROM study_sets")
    suspend fun getAllStudySets() : List<StudySetEntity>

    @Query("SELECT * FROM study_sets ORDER BY id DESC LIMIT 1")
    suspend fun getMostRecentStudySet() : StudySetEntity?


}