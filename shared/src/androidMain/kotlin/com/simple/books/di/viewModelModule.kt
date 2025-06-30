package com.simple.books.di

import com.simple.books.detail.presentation.DetailViewModel
import com.simple.books.listing.presentation.ListingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val viewModelModule = module {
    viewModel { ListingViewModel(get(),get()) }
    viewModel{(id: String) ->
        DetailViewModel(id, get())}
}
