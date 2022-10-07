package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val REQUEST_CODE_EDIT = 3

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var addTerm : Button
    private lateinit var showFlashcardsBtn : Button
    private lateinit var studySetNameTxt : TextView

    private lateinit var flashcardRecycler : RecyclerView

    private lateinit var studySetName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study_set)

        addTerm = findViewById(R.id.add_term)
        showFlashcardsBtn = findViewById(R.id.show_flashcard)
        studySetNameTxt = findViewById(R.id.study_set_name)

        flashcardRecycler = findViewById(R.id.study_set_term_list)
        flashcardRecycler.layoutManager = LinearLayoutManager(this)

        studySetName = intent.getStringExtra(EXTRA_SET_NAME).toString() // get the intent sent over
        studySetNameTxt.text = studySetName

    }
}