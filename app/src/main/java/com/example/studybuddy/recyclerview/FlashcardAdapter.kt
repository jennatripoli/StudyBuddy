package com.example.studybuddy.recyclerview

import android.app.Activity
import android.app.Dialog
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.*
import com.example.studybuddy.database.FlashcardEntity

class FlashcardAdapter (val activity: Activity, private val flashcards : List<FlashcardEntity>) : RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {

    /**
     * Handles how each item view behaves
     */
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private var studySetTermTxt : TextView
        private var studySetDefTxt : TextView
        private var editBtn : Button

        fun bind(flashcard : FlashcardEntity) {

            studySetTermTxt.text = flashcard.term
            studySetDefTxt.text = flashcard.definition

            editBtn.setOnClickListener() { // edit the flashcard

                val dialog = Dialog(activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.layout_edit_study_set_term)

                val l = WindowManager.LayoutParams()
                l.copyFrom(dialog.window?.attributes)
                l.width = WindowManager.LayoutParams.MATCH_PARENT
                l.height = WindowManager.LayoutParams.MATCH_PARENT

                val termEditText = dialog.findViewById(R.id.edit_term) as EditText
                val defEditText = dialog.findViewById(R.id.edit_definition) as EditText
                val addBtn = dialog.findViewById(R.id.save_term) as Button
                val deleteBtn = dialog.findViewById(R.id.cancel_term) as Button

                termEditText.setText(flashcard.term) // sets the edit texts for edit to the prev values
                defEditText.setText(flashcard.definition)

                addBtn.setOnClickListener() { // update the flashcard

                    val newTerm = termEditText.text.toString()
                    val newDef = defEditText.text.toString()

                    if(newDef != "" && newTerm != "") {

                        val newFlashcard = FlashcardEntity()
                        newFlashcard.id = flashcard.id
                        newFlashcard.term = newTerm
                        newFlashcard.definition = newDef
                        newFlashcard.studySetName = flashcard.studySetName

                        (activity as AddStudySetActivity).updateFlashcard(newFlashcard)
                        dialog.cancel()
                    }
                }

                deleteBtn.setOnClickListener() { // delete the flashcard
                    (activity as AddStudySetActivity).deleteFlashcard(flashcard)
                    dialog.cancel()
                }

                dialog.show()
                dialog.window?.attributes = l
            }
        }
        init {
            studySetTermTxt = itemView.findViewById(R.id.edit_term)
            studySetDefTxt = itemView.findViewById(R.id.edit_definition)
            editBtn = itemView.findViewById(R.id.term_edit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_study_set_term, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(flashcards[position])
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }
}