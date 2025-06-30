package com.simple.books.listing.presentation

import app.cash.turbine.test
import com.simple.books.listing.application.ListingUseCase
import com.simple.books.listing.application.QueryUseCase
import com.simple.books.listing.presentation.ListingViewModel.Companion.DEFAULT_QUERY
import com.simple.books.mock.FakeBookFactory.Companion.QUERY
import com.simple.books.mock.FakeListingUseCase
import com.simple.books.mock.FakeListingUseCase.Companion.ERROR_MESSAGE
import com.simple.books.mock.FakeQueryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ListingViewModelTest {

    private lateinit var viewModel: ListingViewModel
    private lateinit var listingUseCase: ListingUseCase
    private lateinit var queryUseCase: QueryUseCase

    @BeforeTest
    fun setup() {
        listingUseCase = FakeListingUseCase()
        queryUseCase = FakeQueryUseCase()
        viewModel = ListingViewModel(listingUseCase, queryUseCase)
    }

    @Test
    fun `getLastQuery when called then loads books with default query`() = runTest {
        advanceUntilIdle()

        viewModel.listingState.test {
            awaitItem().also {
                assertEquals(QUERY, it.query)
                assertEquals(2, it.books.size)
                assertFalse(it.loading)
                assertNull(it.error)
            }
        }

    }

    @Test
    fun `getBooks when forceRefresh is true then loading state is true and books are loaded`() = runTest {
        viewModel.getBooks(QUERY, forceRefresh = true)

        advanceUntilIdle()

        viewModel.listingState.test {
            awaitItem().also {
                assertEquals(QUERY, it.query)
                assertEquals(2, it.books.size)
                assertFalse(it.loading)
                assertNull(it.error)
            }
        }
    }

    @Test
    fun `getBooks when query use case return null then search with default query`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val queryUseCase = FakeQueryUseCase(shouldReturnNone = true)
        val viewModel = ListingViewModel(listingUseCase, queryUseCase, dispatcher)

        advanceUntilIdle()

        viewModel.listingState.test {
            awaitItem().also {
                assertEquals(DEFAULT_QUERY, it.query)
                assertTrue(it.books.isEmpty())
            }
        }
    }

    @Test
    fun `getBooks when listing use case throws error then error state is set`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val listingUseCase = FakeListingUseCase(shouldThrowError = true)
        val viewModel = ListingViewModel(listingUseCase, queryUseCase, dispatcher)

        advanceUntilIdle()

        viewModel.listingState.test {
            awaitItem().also {
                assertFalse(it.loading)
                assertEquals(QUERY, it.query)
                assertTrue(it.error!!.startsWith(ListingViewModel.ERROR))
                assertTrue(it.books.isEmpty())
            }
        }
    }

}
