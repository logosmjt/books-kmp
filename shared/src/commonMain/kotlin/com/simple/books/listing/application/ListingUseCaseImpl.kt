package com.simple.books.listing.application

import com.simple.books.data.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListingUseCaseImpl(private val repository: BookRepository): ListingUseCase {

    override fun invoke (query: String, forceRefresh: Boolean): Flow<List<Book>> {
        return repository.getBooks(query, forceRefresh)
            .map { books ->
                books.map { it.toAppModel() }
            }
    }
}
