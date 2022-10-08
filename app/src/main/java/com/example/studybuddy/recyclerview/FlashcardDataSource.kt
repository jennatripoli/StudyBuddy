package com.example.studybuddy.recyclerview

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studybuddy.BaseApplication
import com.example.studybuddy.database.AppDatabase
import com.example.studybuddy.database.FlashcardEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FlashcardDataSource private constructor(coroutineScope: CoroutineScope = GlobalScope) {

    // reference to database
    private val db = AppDatabase.getInstance(BaseApplication.getContext())?.dao

    // list of live data to display, in this case its a list of all study set names
    private lateinit var flashcardLiveData : MutableLiveData<List<FlashcardEntity>>

    // get all current flashcards based on the given study set name
    init {
        coroutineScope.launch {
            flashcardLiveData = MutableLiveData(db?.getAllFlashcards())
        }
    }
    suspend fun updateLiveData(studySetNameString : String) {
        flashcardLiveData.postValue(db?.getFlashcardsForStudySet(studySetNameString))
    }

    /**
     * Insert a flash card into the recycler view
     */
    suspend fun insertFlashcard(term: String, def : String, studySetName: String) {

        // reject invalid flashcards
        if(term == "" || def == "" || studySetName == "") {
            return
        }

        // create new flashcard entity
        val flashcard = FlashcardEntity()
        flashcard.studySetName = studySetName
        flashcard.term = term
        flashcard.definition = def

        // insert into database to give it an ID
        db?.insertFlashcard(flashcard)

        // get the most recently added study flashcard from the database
        var newestCard = db?.getMostRecentFlashcard()

        if(newestCard?.term == "") {
            newestCard = null
        }

        if(newestCard != null) {

            // update the live data
            val currentList = flashcardLiveData.value

            // the the list is null then add first card
            if (currentList == null) {
                flashcardLiveData.postValue(listOf(newestCard))
            } else { // otherwise add the card to the list and post
                val updatedList = currentList.toMutableList()
                updatedList.add(0, newestCard)
                flashcardLiveData.postValue(updatedList)
            }
        }
    }

    /**
     * Deletes a flashcard from the data base and recycler view
     */
    suspend fun deleteFlashcard(flashcard: FlashcardEntity) {

        // delete flashcard from database
        db?.deleteFlashcard(flashcard)

        // remove from live data
        val currentList = flashcardLiveData.value

        if(currentList != null) {
            val updatedList = currentList.toMutableList()

            for(aFlashcard in updatedList) {
                if(aFlashcard.id == flashcard.id ) {
                    updatedList.remove(aFlashcard)
                    break
                }
            }
            flashcardLiveData.postValue(updatedList)
        }
    }

    /**
     * Updates an edited flashcard
     */
    suspend fun updateFlashcard(oldId : Long, newTerm : String, newDef : String, studySetName : String) {

        // reject bad edits
        if(newTerm == "" || newDef == "" || studySetName == "") {
            return
        }

        // create new flashcard with the same ID and study set name
        val updatedFlashcard = FlashcardEntity()
        updatedFlashcard.id = oldId
        updatedFlashcard.term = newTerm
        updatedFlashcard.definition = newDef
        updatedFlashcard.studySetName = studySetName

        // update the new flashcard into the database
        db?.updateFlashcard(updatedFlashcard)

        // update the live data
        val currentList = flashcardLiveData.value

        if(currentList != null) {
            val updatedList = currentList.toMutableList()

            for(flashcard in updatedList) {
                if(flashcard.id == updatedFlashcard.id) {
                    flashcard.term = newTerm
                    flashcard.definition = newDef
                    break
                }
            }
            flashcardLiveData.postValue(updatedList)
        }
    }

    /**
     * Returns all of the live data
     */
    fun getFlashcardList() : LiveData<List<FlashcardEntity>> {
        return flashcardLiveData
    }


    // create static companion object
    companion object {

        @Volatile
        private var INSTANCE: FlashcardDataSource? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FlashcardDataSource()
            }
        }

        fun getDataSource(): FlashcardDataSource {
            return INSTANCE ?: throw IllegalStateException("Error!")
        }
    }



}