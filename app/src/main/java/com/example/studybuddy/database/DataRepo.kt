package com.example.studybuddy.database

import android.content.Context

class DataRepo(context: Context) {

    var db : DAO = AppDatabase.getInstance(context)?.dao!!

    fun insertFlashcard(flashcard : FlashcardEntity) {
        db.insertFlashcard(flashcard)
    }

    fun deleteFlashcard(flashcard : FlashcardEntity) {
        db.deleteFlashcard(flashcard)
    }

    fun insertStudySet(studySet: StudySetEntity) {
        db.insertStudySet(studySet)
    }

    fun deleteStudySet(studySet: StudySetEntity) {
        db.deleteStudySet(studySet)
    }

    fun getAllFlashcards() : List<FlashcardEntity> {
        return db.getAllFlashcards()
    }

    fun getAllStudySets() : List<StudySetEntity> {
        return db.getAllStudySets()
    }

    fun getFlashCardsFromSet(studySet: String) : List<StudySetWithFlashcards> {
        return db.getStudySetWithFlashcards(studySet)
    }


}