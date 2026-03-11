package com.minnolter.flowtrack.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // --- ExpenseEntity Operations ---

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


    // --- ExpenseItemEntity Operations ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseItem(item: ExpenseItemEntity)

    @Delete
    suspend fun deleteExpenseItem(item: ExpenseItemEntity)

    @Query("SELECT * FROM expenseItems ORDER BY title ASC")
    fun getAllExpenseItems(): Flow<List<ExpenseItemEntity>>

    @Query("SELECT * FROM expenseItems WHERE id = :id")
    suspend fun getExpenseItemById(id: Int): ExpenseItemEntity?

    @Query("SELECT COUNT(*) FROM expenseItems")
    fun getCount(): Flow<Int>

    @Query("SELECT DISTINCT category FROM expenseItems")
    fun getCategories(): Flow<List<String>>

    @Query("UPDATE expenseItems SET icon = :newIcon WHERE category = :categoryName")
    suspend fun updateCategoryIcon(categoryName: String, newIcon: String)

}
