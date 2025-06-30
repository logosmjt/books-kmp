package com.simple.books.detail.application

import kotlinx.coroutines.flow.Flow

interface DetailUseCase {
    fun invoke(id: String): Flow<BookDetail>
}
