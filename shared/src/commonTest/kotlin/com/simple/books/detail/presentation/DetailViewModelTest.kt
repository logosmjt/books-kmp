package com.simple.books.detail.presentation

import app.cash.turbine.test
import com.simple.books.detail.application.DetailUseCase
import com.simple.books.mock.FakeDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private lateinit var detailUseCase: DetailUseCase
    private lateinit var viewModel: DetailViewModel

    @BeforeTest
    fun setup() {
        detailUseCase = FakeDetailUseCase()
    }

    @Test
    fun `getBook when called then emits loading and book detail`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        viewModel = DetailViewModel("id", detailUseCase, dispatcher)

        advanceUntilIdle()
        viewModel.detailState.test {
            val successState = awaitItem()
            assertFalse(successState.loading)
            assertEquals(FakeDetailUseCase.defaultBookDetail, successState.book)
            assertNull(successState.error)
        }
    }

    @Test
    fun `getBook when use case throws error then emits error state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val detailUseCase = FakeDetailUseCase(shouldThrowError = true)
        val viewModel = DetailViewModel("id", detailUseCase, dispatcher)

        advanceUntilIdle()

        viewModel.detailState.test {
            awaitItem().also {
                assertFalse(it.loading)
                assertNotNull(it.error)
                assertTrue(it.error.startsWith(DetailViewModel.ERROR))
                assertNull(it.book)
            }

        }
    }
}
