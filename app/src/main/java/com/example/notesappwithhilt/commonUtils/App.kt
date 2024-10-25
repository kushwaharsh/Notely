package com.example.notesappwithhilt.commonUtils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate() {
        super.onCreate()
        app = this
        prefManager = PrefManager.get(this)
    }

    companion object {
        lateinit var app: App
    }
}