package com.example.w3wthemovie.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(
    productPath: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { productPath().toPath() }
    )
}

internal const val USER_DATA_PREFERENCES_NAME = "user_data_preferences.preferences_pb"