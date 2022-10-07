package com.example.studybuddy.recyclerview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.database.StudySetEntity

class StudySetAdapter(val activity: Activity, private val studySets : List<StudySetEntity>)
    : RecyclerView.Adapter<StudySetAdapter.ViewHolder>()
{

    /**
     * Handles how each item view behaves
     */
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private var studySetNameTxt : TextView
        private var studySetEditButton : Button
        private var studySetDeleteButton : Button

        /**
         * Bind the study set name to the item holder
         */
        fun bind(studySetName : String) {
            // bind the text view to the study set name
            studySetNameTxt.text = studySetName
        }

        init {
            studySetNameTxt = itemView.findViewById(R.id.study_set_name)
            studySetEditButton = itemView.findViewById(R.id.study_set_edit)
            studySetDeleteButton = itemView.findViewById(R.id.study_set_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_study_set, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studySets[position].studySetName)
    }

    override fun getItemCount(): Int {
        return studySets.size
    }


}