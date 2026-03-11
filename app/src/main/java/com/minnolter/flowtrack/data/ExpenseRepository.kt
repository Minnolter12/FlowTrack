package com.minnolter.flowtrack.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val dao: ExpenseDao
) {

    // --- ExpenseEntity ---
    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()
    val totalExpense: Flow<Int?> = dao.getTotalExpense()
    val recentExpenses: Flow<List<ExpenseEntity>> = dao.getRecentExpenses()

    val getCategories: Flow<List<String>> = dao.getCategories()

    val getCount: Flow<Int> = dao.getCount()

    suspend fun insert(expense: ExpenseEntity) {
        dao.insertExpense(expense)
    }

    suspend fun delete(expense: ExpenseEntity) {
        dao.deleteExpense(expense)
    }


    // --- ExpenseItemEntity (New) ---
    val allExpenseItems: Flow<List<ExpenseItemEntity>> = dao.getAllExpenseItems()

    suspend fun insertItem(item: ExpenseItemEntity) {
        dao.insertExpenseItem(item)
    }

    suspend fun deleteItem(item: ExpenseItemEntity) {
        dao.deleteExpenseItem(item)
    }

    suspend fun getItemById(id: Int): ExpenseItemEntity? {
        return dao.getExpenseItemById(id)
    }

    suspend fun updateCategoryIcon(categoryName: String, newIcon: String) {
        dao.updateCategoryIcon(categoryName, newIcon)
    }
}
