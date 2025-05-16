package com.example.quotex

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(primaryKeys = ["id","quote","author"])
@Entity
data class Quote(
    @ColumnInfo(name = "quote") var quote: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
