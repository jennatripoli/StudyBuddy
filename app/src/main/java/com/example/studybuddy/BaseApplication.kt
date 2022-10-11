package com.example.studybuddy

import android.app.Application
import android.content.Context
import com.example.studybuddy.recyclerview.FlashcardDataSource
import com.example.studybuddy.recyclerview.StudySetDataSource

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        StudySetDataSource.initialize(this)
        FlashcardDataSource.initialize(this)
    }

    init {
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: BaseApplication? = null

        fun getContext() : Context {
            return INSTANCE!!.applicationContext
        }
    }
}