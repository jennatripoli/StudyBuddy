package com.example.studybuddy.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val CURRENT_DEF_KEY = "CURRENT_DEF_KEY"

class WebserverDefViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // saves the current definition on screen rotation
    var currentDef : String?
        get() = savedStateHandle[CURRENT_DEF_KEY]
        set(value) = savedStateHandle.set(CURRENT_DEF_KEY, value)

}