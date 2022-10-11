package com.example.studybuddy

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.database.FlashcardEntity
import com.example.studybuddy.recyclerview.FlashcardAdapter
import com.example.studybuddy.recyclerview.FlashcardDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val REQUEST_CODE_FLASHCARD = 4
const val EXTRA_FLASHCARD_SET = "com.example.studybuddy.FLASHCARD_SET"

class AddStudySetActivity : AppCompatActivity() {

    private lateinit var buttonAddTerm : Button
    private lateinit var buttonFlashcards : Button
    private lateinit var textStudySetName : TextView
    private lateinit var stringStudySetName : String

    private lateinit var flashcardTermList : RecyclerView
    private lateinit var flashcardAdapter: FlashcardAdapter
    private lateinit var flashcardDataSource: FlashcardDataSource

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_set)

        buttonAddTerm = findViewById(R.id.add_term)
        buttonFlashcards = findViewById(R.id.play_flashcard)
        textStudySetName = findViewById(R.id.study_set_name)

        stringStudySetName = intent.getStringExtra(EXTRA_SET_NAME).toString()
        textStudySetName.text = stringStudySetName

        flashcardTermList = findViewById(R.id.study_set_term_list)
        flashcardTermList.layoutManager = LinearLayoutManager(this)

        flashcardDataSource = FlashcardDataSource.getDataSource()

        // only want flashcards of this study set to populate the recycler view
        GlobalScope.launch {
            flashcardDataSource.updateLiveData(stringStudySetName)
        }

        val liveData = flashcardDataSource.getFlashcardList()

        liveData.observe(this) {
            it?.let {
                flashcardAdapter = FlashcardAdapter(this, it)
                flashcardTermList.adapter = flashcardAdapter
            }
        }


        buttonAddTerm.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.layout_edit_study_set_term)

            val l = WindowManager.LayoutParams()
            l.copyFrom(dialog.window?.attributes)
            l.width = WindowManager.LayoutParams.MATCH_PARENT
            l.height = WindowManager.LayoutParams.MATCH_PARENT

            val editTextTerm = dialog.findViewById(R.id.study_set_term) as EditText
            val editTextDefinition = dialog.findViewById(R.id.study_set_definition) as EditText
            val buttonSaveTerm = dialog.findViewById(R.id.save_term) as Button
            val buttonDeleteTerm = dialog.findViewById(R.id.delete_term) as Button

            val textCancelTerm = dialog.findViewById(R.id.delete_term_caption) as TextView
            textCancelTerm.text = getString(R.string.cancel_term)

            // add the flashcard
            buttonSaveTerm.setOnClickListener {
                val term = editTextTerm.text.toString()
                val def = editTextDefinition.text.toString()
                if (term != "" && def != "") {
                    GlobalScope.launch(Dispatchers.IO) {
                        flashcardDataSource.insertFlashcard(term, def, stringStudySetName)
                        dialog.cancel()
                    }
                }
            }

            // close the dialog
            buttonDeleteTerm.setOnClickListener {
                dialog.cancel()
            }

            dialog.show()
            dialog.window?.attributes = l
        }

        buttonFlashcards.setOnClickListener {
            // ensure there is actual data
            if(liveData.value?.isNotEmpty() == true) {
                // send the name of the study set to the flashcard activity
                val intent = Intent(this@AddStudySetActivity, FlashcardActivity::class.java).apply {
                    putExtra(
                        EXTRA_FLASHCARD_SET,
                        stringStudySetName
                    )
                }
                startActivityForResult(intent, REQUEST_CODE_FLASHCARD)
            } else {
                val toast = Toast.makeText(applicationContext, "Please Add a Term First!", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    fun updateFlashcard(flashcard: FlashcardEntity) {
        GlobalScope.launch {
            flashcardDataSource.updateFlashcard(flashcard.id, flashcard.term, flashcard.definition, flashcard.studySetName)
        }
    }

    fun deleteFlashcard(flashcard : FlashcardEntity) {
        GlobalScope.launch {
            flashcardDataSource.deleteFlashcard(flashcard)
        }
    }
}