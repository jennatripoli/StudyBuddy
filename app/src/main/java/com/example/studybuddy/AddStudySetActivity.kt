package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

private const val REQUEST_CODE_EDIT = 3

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var addTerm : Button
    //private lateinit var editTerm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study_set)

        addTerm = findViewById(R.id.add_term)
        //editTerm = findViewById(R.id.term_edit)

        addTerm.setOnClickListener {
            val intent = Intent(this@AddStudySetActivity, EditStudySetTermActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }

        /*editTerm.setOnClickListener {
            val intent = Intent(this@AddStudySetActivity, EditStudySetTermActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }*/
    }
}