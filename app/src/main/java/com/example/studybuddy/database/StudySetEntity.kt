package com.example.studybuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sets")
class StudySetEntity(
        @PrimaryKey(autoGenerate = true)
        var id : Long = 0L,
        var studySetName : String = ""
        )