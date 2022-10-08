package com.example.studybuddy.recyclerview

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studybuddy.BaseApplication
import com.example.studybuddy.database.AppDatabase
import com.example.studybuddy.database.StudySetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudySetDataSource private constructor(coroutineScope: CoroutineScope = GlobalScope) {

    // reference to database
    private val db = AppDatabase.getInstance(BaseApplication.getContext())?.dao

    // list of live data to display, in this case its a list of all study set names
    private lateinit var studySetLiveData : MutableLiveData<List<StudySetEntity>>

    // get all current study sets from database on init
    init {
        coroutineScope.launch {
           studySetLiveData = MutableLiveData(db?.getAllStudySets())
        }
    }

    /**
     * Insert a study set into the recycler view
     */
    suspend fun insertStudySet(name : String?) {

        // reject invalid names
        if(name == "" || name == null) {
            return
        }

        // create new study set entity
        val set = StudySetEntity()
        set.studySetName = name

        // insert into database to give it an ID
        db?.insertStudySet(set)

        // get the most recently added study set from the database
        var newestSet = db?.getMostRecentStudySet()

        if(newestSet?.studySetName == "") {
            newestSet = null
        }

        if(newestSet != null) {

            // update the live data
            val currentList = studySetLiveData.value

            // the the list is null then add first set
            if (currentList == null) {
                studySetLiveData.postValue(listOf(newestSet))
            } else { // otherwise add the set to the list and post
                val updatedList = currentList.toMutableList()
                updatedList.add(0, newestSet)
                studySetLiveData.postValue(updatedList)
            }
        }
    }

    /**
     * Update the study-set in the recycler view
     */
    suspend fun updateStudySet(position : Int, id : Long, name : String) {
        // update the new study set in the database
        val newSet = StudySetEntity(id, name)
        db?.updateStudySet(newSet)

        // update the livedata
        val currentList = studySetLiveData.value
        if(currentList == null) {
            studySetLiveData.postValue(listOf(newSet))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList[position] = newSet
            studySetLiveData.postValue(updatedList)
        }
    }

    /**
     *  Deletes a study set from the database
     */
    suspend fun deleteStudySet(studySetName : String) {
        // remove from database
        db?.deleteStudySet(studySetName)

        // remove all flashcards from database associated with that study set
        db?.deleteAllFlashcardsFromSet(studySetName)

        // remove from live data
        val currentList = studySetLiveData.value
        if(currentList != null) {
            val updatedList = currentList.toMutableList()

            // search list for study set of the given name
            for(studySet in updatedList) {
                if(studySet.studySetName == studySetName) {
                    updatedList.remove(studySet)
                    break
                }
            }
            studySetLiveData.postValue(updatedList)
        }
    }

    /**
     * Returns all of the live data
     */
    fun getStudySetList() : LiveData<List<StudySetEntity>> {
        return studySetLiveData
    }

    // create static companion object
    companion object {

        @Volatile
        private var INSTANCE: StudySetDataSource? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = StudySetDataSource()
            }
        }

        fun getDataSource(): StudySetDataSource {
            return INSTANCE ?: throw IllegalStateException("Error!")
        }
    }
}