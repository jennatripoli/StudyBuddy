package com.example.studybuddy

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.database.FlashcardEntity
import com.example.studybuddy.recyclerview.FlashcardAdapter
import com.example.studybuddy.recyclerview.FlashcardDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var addTerm : Button
    private lateinit var showFlashcardsBtn : Button
    private lateinit var studySetNameTxt : TextView

    private lateinit var flashcardRecycler : RecyclerView
    private lateinit var flashcardAdapter: FlashcardAdapter
    private lateinit var flashcardDataSource: FlashcardDataSource

    private lateinit var studySetName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study_set)

        addTerm = findViewById(R.id.add_term)
        showFlashcardsBtn = findViewById(R.id.show_flashcard)
        studySetNameTxt = findViewById(R.id.study_set_name)

        studySetName = intent.getStringExtra(EXTRA_SET_NAME).toString() // get the intent sent over
        studySetNameTxt.text = studySetName

        flashcardRecycler = findViewById(R.id.study_set_term_list)
        flashcardRecycler.layoutManager = LinearLayoutManager(this)

        flashcardDataSource = FlashcardDataSource.getDataSource()
        flashcardDataSource.setFlashcardList(studySetName)
        val liveData = flashcardDataSource.getFlashcardList()

        liveData.observe(this) {
            it?.let {
                flashcardAdapter = FlashcardAdapter(this, it)
                flashcardRecycler.adapter = flashcardAdapter
            }
        }

        addTerm.setOnClickListener() {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.activity_edit_study_set_term)

            val l = WindowManager.LayoutParams()
            l.copyFrom(dialog.window?.attributes)
            l.width = WindowManager.LayoutParams.MATCH_PARENT
            l.height = WindowManager.LayoutParams.MATCH_PARENT

            val termEditText = dialog.findViewById(R.id.study_set_term) as EditText
            val defEditText = dialog.findViewById(R.id.study_set_definition) as EditText
            val addBtn = dialog.findViewById(R.id.save_term) as Button
            val deleteBtn = dialog.findViewById(R.id.delete_term) as Button // TODO: Implement this

            addBtn.setOnClickListener() {
                val term = termEditText.text.toString()
                val def = defEditText.text.toString()
                if (term != "" && def != "") {
                    Log.d("MAIN", "They were not null")
                    GlobalScope.launch(Dispatchers.IO) {
                        flashcardDataSource.insertFlashcard(term, def, studySetName)
                        dialog.cancel()
                    }
                }
            }
            dialog.show()
            dialog.window?.attributes = l
        }
    }
}