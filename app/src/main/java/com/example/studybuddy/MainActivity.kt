package com.example.studybuddy

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.recyclerview.StudySetAdapter
import com.example.studybuddy.recyclerview.StudySetDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val REQUEST_CODE_ADD_SET = 1
const val REQUEST_CODE_POMODORO = 2
const val REQUEST_CODE_DICTIONARY = 3
const val EXTRA_SET_NAME = "com.example.studybuddy.SET_NAME"

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAddStudySet : Button
    private lateinit var buttonPomodoro : Button
    private lateinit var buttonDictionary : Button

    private lateinit var studySetRecycler : RecyclerView
    private lateinit var studySetAdapter : StudySetAdapter
    private lateinit var setDataSource: StudySetDataSource

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddStudySet = findViewById(R.id.add_study_set)
        buttonPomodoro = findViewById(R.id.study_plan)
        buttonDictionary = findViewById(R.id.log_in)

        studySetRecycler = findViewById(R.id.study_set_list)
        studySetRecycler.layoutManager = LinearLayoutManager(this)

        setDataSource = StudySetDataSource.getDataSource()
        val liveData = setDataSource.getStudySetList()

        liveData.observe(this) {
            it?.let {
                studySetAdapter = StudySetAdapter(this, it)
                studySetRecycler.adapter = studySetAdapter
            }
        }

        buttonAddStudySet.setOnClickListener {
            createNewStudySet()
        }

        buttonPomodoro.setOnClickListener {
            val intent = Intent(this@MainActivity, PomodoroActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_POMODORO)
        }

        buttonDictionary.setOnClickListener() {
            val intent = Intent(this@MainActivity, WebserverActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_DICTIONARY)
        }
    }

    private fun createNewStudySet() {
        // create alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Study Set Name")

        // create text input
        val newSetNameInput = EditText(this)
        newSetNameInput.inputType = InputType.TYPE_CLASS_TEXT

        // apply text input to builder
        builder.setView(newSetNameInput)

        // create buttons for interaction
        builder.setPositiveButton("Create") { _, _ ->

            val newSetName = newSetNameInput.text.toString()

            // add to recycler view and database
            if(newSetName != "") {
                GlobalScope.launch(Dispatchers.IO) {
                    setDataSource.insertStudySet(newSetName)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    // remove from recycler view and database
    fun deleteStudySet(name : String) {
        if(name != "") {
            GlobalScope.launch(Dispatchers.IO) {
                setDataSource.deleteStudySet(name)
            }
        }
    }
}


