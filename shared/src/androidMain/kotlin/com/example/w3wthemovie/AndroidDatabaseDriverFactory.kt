package com.example.w3wthemovie

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.w3wthemovie.data.local.DatabaseDriverFactory

class AndroidDatabaseDriverFactory(
    private val context: Context,
): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            W3WTheMovieDatabase.Schema,
            context,
            "w3w_the_movie.db",
        )
    }
}
