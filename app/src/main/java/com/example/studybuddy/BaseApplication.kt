package com.example.studybuddy

import android.app.Application
import android.content.Context
import com.example.studybuddy.recyclerview.StudySetDataSource

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // init our recycler view data sources
        StudySetDataSource.initialize(this)

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