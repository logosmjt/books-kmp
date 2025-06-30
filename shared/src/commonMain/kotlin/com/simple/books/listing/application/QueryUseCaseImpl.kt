package com.simple.books.listing.application

import com.simple.books.data.BookRepository

class QueryUseCaseImpl(private val repository: BookRepository) : QueryUseCase {

    override suspend fun invoke(): String? {
        return repository.getLastQuery()
    }
}
