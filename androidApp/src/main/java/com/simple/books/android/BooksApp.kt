package com.simple.books.android

import android.app.Application
import com.simple.books.android.di.androidModule
import com.simple.books.di.KoinInit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class BooksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInit().init {
//            androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@BooksApp)
            modules(
                listOf(
                    androidModule,
                ),
            )
        }
    }
}
