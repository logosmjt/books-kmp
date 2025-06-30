package com.simple.books.listing.application

import com.simple.books.data.BookRepository
import com.simple.books.mock.FakeBookRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class QueryUseCaseTest {

    private lateinit var repository: BookRepository
    private lateinit var useCase: QueryUseCase

    @BeforeTest
    fun setup() {
        repository = FakeBookRepository()
        useCase = QueryUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns query`() = runTest {
        val result = useCase.invoke()
        assertEquals("kotlin", result)
    }

    @Test
    fun `invoke when repository fails then throws exception`() = runTest {
        val errorRepo = FakeBookRepository(shouldThrowError = true)
        val failingUseCase = QueryUseCaseImpl(errorRepo)

        assertFailsWith<RuntimeException> {
            failingUseCase.invoke()
        }
    }
}
