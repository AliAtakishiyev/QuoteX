package com.example.quotex

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDAO {

    @Insert
    suspend fun addQuote(vararg quotes: Quote)

    @Delete
    suspend fun deleteQuote(vararg quotes: Quote)

    @Update
   suspend fun updateQuote(vararg quotes: Quote)

    @Query("SELECT * FROM quote")
     fun loadAllQuotes(): Flow<List<Quote>>
}