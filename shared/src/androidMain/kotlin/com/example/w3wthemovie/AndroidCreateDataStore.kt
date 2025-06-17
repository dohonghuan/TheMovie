package com.example.w3wthemovie

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.w3wthemovie.data.local.USER_DATA_PREFERENCES_NAME

fun createDataStore(context: Context): DataStore<Preferences> {
    return com.example.w3wthemovie.data.local.createDataStore {
        context.filesDir.resolve(USER_DATA_PREFERENCES_NAME).absolutePath
    }
}
