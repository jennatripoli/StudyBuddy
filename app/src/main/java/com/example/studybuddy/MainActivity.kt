package com.example.studybuddy

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.recyclerview.StudySetAdapter
import com.example.studybuddy.recyclerview.StudySetDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
private const val EXTRA_SET_NAME = "com.example.studybuddy.SET_NAME"

private const val REQUEST_CODE_ADD_SET = 1
private const val REQUEST_CODE_POMODORO = 2

class MainActivity : AppCompatActivity() {

    // on page widgets
    private lateinit var buttonAddStudySet : Button
    private lateinit var buttonPomodoro : Button

    // study set RecyclerView setup
    private lateinit var studySetRecycler : RecyclerView
    private lateinit var studySetAdapter : StudySetAdapter
    private lateinit var setDataSource: StudySetDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init widgets
        buttonAddStudySet = findViewById(R.id.add_study_set)
        buttonPomodoro = findViewById(R.id.study_plan)

        // init recycler view
        studySetRecycler = findViewById(R.id.study_set_list)
        studySetRecycler.layoutManager = LinearLayoutManager(this)

        // setup data source
        setDataSource = StudySetDataSource.getDataSource()
        val liveData = setDataSource.getStudySetList()

        // live data observer
        liveData.observe(this) {
            it?.let {
                studySetAdapter = StudySetAdapter(this, it)
                studySetRecycler.adapter = studySetAdapter
            }
        }

        // on create study set
        buttonAddStudySet.setOnClickListener {

            // create alert dialog
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Study Set Name")

            // create text input
            val newSetNameInput = EditText(this)
            newSetNameInput.inputType = InputType.TYPE_CLASS_TEXT

            // apply text input to builder
            builder.setView(newSetNameInput)

            // create button
            builder.setPositiveButton("Create") { _, _ ->

                // get text input
                val newSetName = newSetNameInput.text.toString()

                // if we have a valid name
                if(newSetName != "") {

                    GlobalScope.launch(Dispatchers.IO) {

                        // insert to db and recycler view
                        setDataSource.insertStudySet(newSetName)

                        // start intent and send study set name via extra so we know what flashcards to display
                        val intent = Intent(this@MainActivity, AddStudySetActivity::class.java).apply {
                            putExtra(EXTRA_SET_NAME, newSetName)
                        }
                        resultLauncher.launch(intent)
                    }
                }
            }
            // cancel button
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            // show alert dialog
            builder.show()
        }

        // on pomodoro start
        buttonPomodoro.setOnClickListener {
            val intent = Intent(this@MainActivity, PomodoroActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_POMODORO)
        }
    }

    // activity result callback non-deprecated
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Finished Activity")
        }
    }


}

