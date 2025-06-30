package com.simple.books.di

import com.simple.books.common.Logger
import com.simple.books.data.BookDataSource
import com.simple.books.data.BookRepository
import com.simple.books.data.BookRepositoryImpl
import com.simple.books.data.BookService
import com.simple.books.data.BookServiceImpl
import com.simple.books.detail.application.DetailUseCase
import com.simple.books.detail.application.DetailUseCaseImpl
import com.simple.books.listing.application.ListingUseCase
import com.simple.books.listing.application.ListingUseCaseImpl
import com.simple.books.listing.application.QueryUseCase
import com.simple.books.listing.application.QueryUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import io.ktor.client.plugins.logging.Logger as KtorLogger

val commonModule = module {

    single<HttpClient> {
        HttpClient {
            followRedirects = true
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        Logger.d("KtorLogger", "message:$message")
                    }
                }

                level = LogLevel.INFO
            }

            install(HttpTimeout) {
                val timeout = 30000L
                connectTimeoutMillis = timeout
                requestTimeoutMillis = timeout
                socketTimeoutMillis = timeout
            }
        }
    }

    single<BookDataSource> { BookDataSource(getOrNull()) }
    single<BookService> { BookServiceImpl(get()) }
    single<BookRepository> { BookRepositoryImpl(get(), get()) }

    single<ListingUseCase> { ListingUseCaseImpl(get()) }
    single<QueryUseCase> { QueryUseCaseImpl(get()) }
    single<DetailUseCase> { DetailUseCaseImpl(get()) }
}
