package com.example.w3wthemovie

object IosDataHelper {
    val appContainer by lazy {
        AppContainer(
            databaseDriverFactory = IOSDatabaseDriverFactory(),
            dataStore = createDataStoreFactory()
        )
    }
}