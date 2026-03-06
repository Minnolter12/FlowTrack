package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financeapp.data.ExpenseRepository
import com.example.financeapp.data.UserPreferencesRepository

class AppViewModelFactory(
    private val repository: ExpenseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
