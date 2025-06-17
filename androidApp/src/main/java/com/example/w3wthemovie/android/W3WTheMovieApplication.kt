package com.example.w3wthemovie.android

import android.app.Application
import com.example.w3wthemovie.AndroidDatabaseDriverFactory
import com.example.w3wthemovie.AppContainer
import com.example.w3wthemovie.createDataStore

class W3WTheMovieApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(
            databaseDriverFactory = AndroidDatabaseDriverFactory(context = applicationContext),
            dataStore = createDataStore(context = applicationContext),
        )
    }
}