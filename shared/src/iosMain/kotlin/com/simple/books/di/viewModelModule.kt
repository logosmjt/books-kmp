package com.simple.books.di

import com.simple.books.detail.presentation.DetailViewModel
import com.simple.books.listing.presentation.ListingViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val viewModelModule = module {
    single<CoroutineDispatcher> { Dispatchers.Default }
    singleOf(::ListingViewModel)
    factory { (id: String) -> DetailViewModel(id, get()) }
}
