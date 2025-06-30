package com.simple.books.listing.application

import kotlinx.coroutines.flow.Flow

interface ListingUseCase {
    fun invoke (query: String, forceRefresh: Boolean = false): Flow<List<Book>>
}
