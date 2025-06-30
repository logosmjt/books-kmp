package com.simple.books

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.simple.books.db.BooksDatabase

internal actual fun testDbConnection(): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    .also { BooksDatabase.Schema.create(it) }
