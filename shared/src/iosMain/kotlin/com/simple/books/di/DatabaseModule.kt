package com.simple.books.di

import app.cash.sqldelight.db.SqlDriver
import com.simple.books.db.BooksDatabase
import com.simple.books.db.DatabaseDriverFactory
import org.koin.dsl.module

actual val databaseModule = module {

    single<SqlDriver> {
        DatabaseDriverFactory().createDriver()
    }

    single<BooksDatabase> { BooksDatabase(get<SqlDriver>()) }
}
