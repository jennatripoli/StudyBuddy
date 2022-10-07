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

        if(newestCard?.studySetName == "") {
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
     * Returns all of the live data
     */
    fun getFlashcardList() : LiveData<List<FlashcardEntity>> {
        return flashcardLiveData
    }

    /**
     * Set the live data to only be flashcards of a given study set
     */
    fun setFlashcardList(studySetName : String) {
        GlobalScope.launch {
            flashcardLiveData = MutableLiveData(db?.getFlashcardsForStudySet(studySetName))
        }
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