package com.example.studybuddy.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class FlashcardActivityViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // saves the index of list on screen rotation
    var currentIndex: Int?
        get() = savedStateHandle[CURRENT_INDEX_KEY]
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)



}