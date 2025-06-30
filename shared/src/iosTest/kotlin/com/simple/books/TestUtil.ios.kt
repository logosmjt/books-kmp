package com.simple.books

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import com.simple.books.db.BooksDatabase

internal actual fun testDbConnection(): SqlDriver = inMemoryDriver(BooksDatabase.Schema)
