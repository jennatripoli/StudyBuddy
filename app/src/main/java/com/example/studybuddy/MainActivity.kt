package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.database.DataRepo

private const val REQUEST_CODE_ADD_SET = 1
private const val REQUEST_CODE_POMODORO = 2

class MainActivity : AppCompatActivity() {

    private lateinit var studySetList : RecyclerView
    private lateinit var buttonAddStudySet : Button
    private lateinit var buttonPomodoro : Button
    private lateinit var database : DataRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = DataRepo(applicationContext)

        studySetList = findViewById(R.id.study_set_list)
        buttonAddStudySet = findViewById(R.id.add_study_set)
        buttonPomodoro = findViewById(R.id.study_plan)

        studySetList.layoutManager = LinearLayoutManager(this)

        buttonAddStudySet.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStudySetActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_SET)
        }

        buttonPomodoro.setOnClickListener {
            val intent = Intent(this@MainActivity, PomodoroActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_POMODORO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
    }

}

