package com.simple.books.detail.application

import com.simple.books.data.BookRepository
import com.simple.books.listing.application.ListingUseCaseImpl
import com.simple.books.mock.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DetailUseCaseTest {

    private lateinit var repository: BookRepository
    private lateinit var useCase: DetailUseCase

    @BeforeTest
    fun setup() {
        repository = FakeBookRepository()
        useCase = DetailUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns mapped book list`() = runTest {
        val result = useCase.invoke("vol123").first()

        assertEquals("Fake Book Title", result.title)
        assertEquals("By John Doe, Jane Smith", result.authors)
        assertEquals("Fake Publisher", result.publisher)
    }

    @Test
    fun `invoke when repository fails then throws exception`() = runTest {
        val errorRepo = FakeBookRepository(shouldThrowError = true)
        val failingUseCase = ListingUseCaseImpl(errorRepo)

        assertFailsWith<RuntimeException> {
            failingUseCase.invoke("vol123").first()
        }
    }
}
