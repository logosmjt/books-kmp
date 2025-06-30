package com.simple.books.detail.application

import com.simple.books.data.BookRepository
import com.simple.books.data.toBookDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailUseCaseImpl(private val repository: BookRepository) : DetailUseCase {

    override fun invoke(id: String): Flow<BookDetail> {
        return repository.getBookDetail(id).map { it -> it.toBookDetail() }
    }
}
