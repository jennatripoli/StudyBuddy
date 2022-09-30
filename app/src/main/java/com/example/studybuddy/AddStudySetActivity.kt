package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var studySetTermList : RecyclerView
    private lateinit var studySetName : EditText
    private lateinit var addTerm : Button
    private lateinit var showFlashCard : Button
    private lateinit var testStart : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study_set)

        studySetTermList = findViewById(R.id.study_set_term_list)
        studySetName = findViewById(R.id.study_set_name)
        addTerm = findViewById(R.id.add_term)
        showFlashCard = findViewById(R.id.show_flashcard)
        testStart = findViewById(R.id.test_start)

    }
}