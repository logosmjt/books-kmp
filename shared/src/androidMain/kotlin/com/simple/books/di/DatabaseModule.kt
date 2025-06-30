package com.simple.books.di

import app.cash.sqldelight.db.SqlDriver
import com.simple.books.db.BooksDatabase
import com.simple.books.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {

    single<SqlDriver> { DatabaseDriverFactory(androidContext()).createDriver() }

    single<BooksDatabase> { BooksDatabase(get<SqlDriver>()) }
}
