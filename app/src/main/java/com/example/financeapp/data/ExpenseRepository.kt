package com.example.financeapp.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val dao: ExpenseDao
) {

    val allExpenses: Flow<List<ExpenseEntity>> =
        dao.getAllExpenses()

    val totalExpense: Flow<Int?> =
        dao.getTotalExpense()

    val recentExpenses: Flow<List<ExpenseEntity>> =
        dao.getRecentExpenses()


    suspend fun insert(expense: ExpenseEntity) {
        dao.insertExpense(expense)
    }

    suspend fun delete(expense: ExpenseEntity) {
        dao.deleteExpense(expense)
    }
}
