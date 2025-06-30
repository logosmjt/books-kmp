package com.simple.books.listing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.books.listing.application.ListingUseCase
import com.simple.books.listing.application.QueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ListingViewModel(
    private val listingUseCase: ListingUseCase,
    private val queryUseCase: QueryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val _listingState: MutableStateFlow<ListingState> =
        MutableStateFlow(ListingState())

    val listingState: StateFlow<ListingState> = _listingState

    init {
        getLastQuery()
    }

    fun getLastQuery() {
        viewModelScope.launch(dispatcher) {
            val lastQuery = queryUseCase.invoke() ?: DEFAULT_QUERY
            getBooks(lastQuery)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getBooks(query: String, forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            listingUseCase.invoke(query = query, forceRefresh = forceRefresh)
                .onStart {
                    _listingState.emit(
                        ListingState(
                            loading = true,
                            query = query,
                            books = _listingState.value.books
                        )
                    )
                }
                .catch {
                    _listingState.emit(ListingState(query = query, error = ERROR))
                }.collect {
                    _listingState.emit(ListingState(query = query, books = it))
                }
        }
    }

    companion object {
        const val DEFAULT_QUERY = "hello"
        const val ERROR = "There might be something wrong when fetch books."
    }
}
