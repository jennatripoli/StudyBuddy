package com.example.studybuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sets")
data class StudySetEntity (
        @PrimaryKey(autoGenerate = false)
        val studySetName : String
        )