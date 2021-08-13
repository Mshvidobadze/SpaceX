package com.example.spacex.entity

import android.content.Context
import com.example.spacex.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

class DatabaseDriverFactory(private val context: Context) {
     fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema,context, "testData.db")
    }
}