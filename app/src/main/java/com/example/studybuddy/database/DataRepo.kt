package com.example.studybuddy.database

import android.content.Context

class DataRepo(context: Context) {

    var db : DAO = AppDatabase.getInstance(context)?.dao!!

    suspend fun insertFlashcard(flashcard : FlashcardEntity) {
        db.insertFlashcard(flashcard)
    }

    suspend fun deleteFlashcard(flashcard : FlashcardEntity) {
        db.deleteFlashcard(flashcard)
    }

    suspend fun insertStudySet(studySet: StudySetEntity) {
        db.insertStudySet(studySet)
    }

    suspend fun deleteStudySet(studySet: StudySetEntity) {
        db.deleteStudySet(studySet)
    }

    suspend fun getAllFlashcards() : List<FlashcardEntity> {
        return db.getAllFlashcards()
    }

    suspend fun getAllStudySets() : List<StudySetEntity> {
        return db.getAllStudySets()
    }

    suspend fun getFlashCardsFromSet(studySet: String) : List<StudySetWithFlashcards> {
        return db.getStudySetWithFlashcards(studySet)
    }


}