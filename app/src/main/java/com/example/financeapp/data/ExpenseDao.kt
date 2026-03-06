package com.example.financeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpense(): Flow<Int?>

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC LIMIT 5")
    fun getRecentExpenses(): Flow<List<ExpenseEntity>>
}
