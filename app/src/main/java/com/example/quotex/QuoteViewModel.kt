package com.example.quotex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuotesRepository): ViewModel() {

    val quotes: StateFlow<List<Quote>> = repository
        .getQuotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addQuote(quote: Quote){
        viewModelScope.launch {
            repository.addQuote(quote)
        }
    }

    fun updateQuote(quote: Quote){
        viewModelScope.launch {
            repository.updateQuote(quote)
        }
    }

    fun deleteQuote(quote: Quote){
        viewModelScope.launch {
            repository.deleteQuote(quote)
        }
    }

    class QuoteViewModelFactory(
        private val repository: QuotesRepository
    ) : ViewModelProvider.Factory{

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(QuoteViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return QuoteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown Viewmodel class")
        }

    }

}