package com.example.studybuddy.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val CURRENT_STUDY_KEY = "CURRENT_STUDY_KEY"
private const val CURRENT_BREAK_KEY = "CURRENT_BREAK_KEY"
private const val STUDY_ON_KEY = "STUDY_ON_KEY"
private const val BREAK_ON_KEY = "BREAK_ON_KEY"

class PomodoroViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // keeps track of the study timer
    var currentStudyTime : Long?
        get() = savedStateHandle[CURRENT_STUDY_KEY]
        set(value) = savedStateHandle.set(CURRENT_STUDY_KEY, value)

    // keeps track of break timer
    var currentBreakTime : Long?
        get() = savedStateHandle[CURRENT_BREAK_KEY]
        set(value) = savedStateHandle.set(CURRENT_BREAK_KEY, value)

    // keeps track if study timer is on
    var studyOn : Boolean?
        get() = savedStateHandle[STUDY_ON_KEY]
        set(value) = savedStateHandle.set(STUDY_ON_KEY, value)

    // keeps track if break timer is on
    var breakOn : Boolean?
        get() = savedStateHandle[BREAK_ON_KEY]
        set(value) = savedStateHandle.set(BREAK_ON_KEY, value)

}