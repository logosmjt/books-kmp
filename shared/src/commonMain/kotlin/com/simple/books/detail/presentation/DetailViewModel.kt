package com.simple.books.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.books.detail.application.DetailUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(
    private val id: String,
    private val userCase: DetailUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val _detailState: MutableStateFlow<DetailState> =
        MutableStateFlow(DetailState(loading = true))

    val detailState: StateFlow<DetailState> = _detailState

    init {
        getBook()
    }

    fun getBook() {
        viewModelScope.launch(dispatcher) {
            userCase.invoke(id)
                .onStart {
                    _detailState.emit(
                        DetailState(
                            loading = true,
                            book = _detailState.value.book
                        )
                    )
                }
                .catch { _detailState.emit(DetailState(error = ERROR + id)) }
                .collect { _detailState.emit(DetailState(book = it)) }
        }
    }

    companion object {
        const val ERROR = "There might be something wrong when load book: "
    }
}
