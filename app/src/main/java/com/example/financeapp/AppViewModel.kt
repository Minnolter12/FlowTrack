package com.example.financeapp

import com.example.financeapp.data.ExpenseEntity
import com.example.financeapp.data.ExpenseRepository
import com.example.financeapp.data.UserPreferencesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.ExpenseItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: ExpenseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = userPreferencesRepository.isDarkTheme
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true
        )

    val points: StateFlow<Int> = userPreferencesRepository.userPoints
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val expenses: StateFlow<List<ExpenseEntity>> =
        repository.allExpenses.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    val getCategories: StateFlow<List<String>> =
        repository.getCategories.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val getCount: StateFlow<Int> =
        repository.getCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val recentExpenses: StateFlow<List<ExpenseEntity>> =
        repository.recentExpenses.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val expenseItems: StateFlow<List<ExpenseItemEntity>> =
        repository.allExpenseItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )



    val totalExpense: StateFlow<Int> =
        repository.totalExpense
            .map { it ?: 0 }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )

    fun addExpense(title: String, amount: Int) {
        viewModelScope.launch {
            repository.insert(
                ExpenseEntity(
                    title = title,
                    amount = amount
                )
            )
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    fun toggleAppTheme() {
        viewModelScope.launch {
            userPreferencesRepository.saveThemePreference(!isDarkTheme.value)
        }
    }

    fun addPoints(newPoints: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserPoints(points.value + newPoints)
        }
    }

    // Methods Exposing Items Table

    fun addItem(name: String, category: String) {
        viewModelScope.launch {
            repository.insertItem(
                ExpenseItemEntity(
                    title = name,
                    category = category
                )
            )
        }
    }

    fun deleteItem(item: ExpenseItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}
