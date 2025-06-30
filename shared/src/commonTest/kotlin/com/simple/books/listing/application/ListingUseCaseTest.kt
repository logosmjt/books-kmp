package com.simple.books.listing.application

import com.simple.books.data.BookRepository
import com.simple.books.mock.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListingUseCaseTest {

    private lateinit var repository: BookRepository
    private lateinit var useCase: ListingUseCase

    @BeforeTest
    fun setup() {
        repository = FakeBookRepository()
        useCase = ListingUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns mapped book list`() = runTest {
        val result = useCase.invoke("kotlin").first()

        assertEquals(2, result.size)
        assertEquals("Effective Kotlin", result.first().title)
    }

    @Test
    fun `invoke when repository fails then throws exception`() = runTest {
        val errorRepo = FakeBookRepository(shouldThrowError = true)
        val failingUseCase = ListingUseCaseImpl(errorRepo)

        assertFailsWith<RuntimeException> {
            failingUseCase.invoke("kotlin").first()
        }
    }
}
