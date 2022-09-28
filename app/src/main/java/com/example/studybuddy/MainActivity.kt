package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

private const val REQUEST_CODE_ADD_SET = 1

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAddStudySet : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddStudySet = findViewById(R.id.add_study_set)

        buttonAddStudySet.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStudySetActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_SET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
    }

}

