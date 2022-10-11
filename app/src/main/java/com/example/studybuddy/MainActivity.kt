package com.example.studybuddy

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_add_study_set)

        val l = WindowManager.LayoutParams()
        l.copyFrom(dialog.window?.attributes)
        l.width = WindowManager.LayoutParams.MATCH_PARENT
        l.height = WindowManager.LayoutParams.WRAP_CONTENT

        val editTextStudySetName = dialog.findViewById(R.id.edit_term) as EditText
        val buttonSaveStudySet = dialog.findViewById(R.id.save_study_set) as Button
        val buttonCancelStudySet = dialog.findViewById(R.id.cancel_study_set) as Button

        buttonSaveStudySet.setOnClickListener() {
            val newStudySetName = editTextStudySetName.text.toString()

            if(newStudySetName != "") {
                GlobalScope.launch(Dispatchers.IO) {
                    setDataSource.insertStudySet(newStudySetName)
                    dialog.cancel()
                }
            } else {
                val toast = Toast.makeText(applicationContext, "Please enter a name.", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        buttonCancelStudySet.setOnClickListener() {
            dialog.cancel()
        }

        dialog.show()
        dialog.window?.attributes = l
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