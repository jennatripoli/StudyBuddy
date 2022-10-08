package com.example.studybuddy

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

const val REQUEST_CODE_FLASHCARD = 4

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var buttonAddTerm : Button
    private lateinit var buttonFlashcards : Button
    private lateinit var textStudySetName : TextView
    private lateinit var stringStudySetName : String

    private lateinit var flashcardRecycler : RecyclerView
    private lateinit var flashcardAdapter: FlashcardAdapter
    private lateinit var flashcardDataSource: FlashcardDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_study_set)

        buttonAddTerm = findViewById(R.id.add_term)
        buttonFlashcards = findViewById(R.id.show_flashcard)
        textStudySetName = findViewById(R.id.study_set_name)

        stringStudySetName = intent.getStringExtra(EXTRA_SET_NAME).toString() // get the intent sent over
        textStudySetName.text = stringStudySetName

        flashcardRecycler = findViewById(R.id.study_set_term_list)
        flashcardRecycler.layoutManager = LinearLayoutManager(this)

        flashcardDataSource = FlashcardDataSource.getDataSource()

        GlobalScope.launch { // we only want flashcards of this study set to populate the recycler view
            flashcardDataSource.updateLiveData(stringStudySetName)
        }

        val liveData = flashcardDataSource.getFlashcardList()

        liveData.observe(this) {
            it?.let {
                flashcardAdapter = FlashcardAdapter(this, it)
                flashcardRecycler.adapter = flashcardAdapter
            }
        }


        buttonAddTerm.setOnClickListener() {
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
            val deleteBtn = dialog.findViewById(R.id.delete_term) as Button

            val cancelTip = dialog.findViewById(R.id.delete_term_caption) as TextView
            cancelTip.text = getString(R.string.cancel_term)

            addBtn.setOnClickListener() { // add the flashcard
                val term = termEditText.text.toString()
                val def = defEditText.text.toString()
                if (term != "" && def != "") {
                    GlobalScope.launch(Dispatchers.IO) {
                        flashcardDataSource.insertFlashcard(term, def, stringStudySetName)
                        dialog.cancel()
                    }
                }
            }

            deleteBtn.setOnClickListener { // close the dialog
                dialog.cancel()
            }

            dialog.show()
            dialog.window?.attributes = l
        }

        buttonFlashcards.setOnClickListener() {
            val intent = Intent(this@AddStudySetActivity, FlashcardActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_FLASHCARD)
        }
    }

    fun deleteFlashcard(flashcard : FlashcardEntity) {
        GlobalScope.launch {
            flashcardDataSource.deleteFlashcard(flashcard)
        }
    }

}