package com.simple.books.mock

import com.simple.books.listing.application.QueryUseCase
import com.simple.books.mock.FakeBookFactory.Companion.QUERY

class FakeQueryUseCase(private val shouldReturnNone: Boolean = false) : QueryUseCase {
    override suspend fun invoke(): String? {
        if (shouldReturnNone) {
            return null
        } else {
            return QUERY
        }
    }
}
