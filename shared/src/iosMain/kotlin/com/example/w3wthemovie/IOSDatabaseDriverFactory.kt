package com.example.w3wthemovie

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.w3wthemovie.data.local.DatabaseDriverFactory

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(W3WTheMovieDatabase.Schema, "w3w_the_movie.db")
    }
}
