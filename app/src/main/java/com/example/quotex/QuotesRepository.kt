package com.example.quotex

import kotlinx.coroutines.flow.Flow

class QuotesRepository(private val dao: QuoteDAO){

    suspend fun addQuote(quote: Quote){
        dao.addQuote(quote)
    }

    suspend fun  deleteQuote(quote: Quote){
        dao.deleteQuote(quote)
    }

    suspend fun updateQuote(quote: Quote){
        dao.updateQuote(quote)
    }

    fun getQuotes(): Flow<List<Quote>> {
        return dao.loadAllQuotes()
    }
}