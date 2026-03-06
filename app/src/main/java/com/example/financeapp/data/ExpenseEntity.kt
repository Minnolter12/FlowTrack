package com.example.financeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val amount: Int,
    val timestamp: Long = System.currentTimeMillis()
)
