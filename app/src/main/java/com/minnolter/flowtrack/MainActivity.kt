package com.minnolter.flowtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.minnolter.flowtrack.app.AppViewModel
import com.minnolter.flowtrack.app.AppViewModelFactory
import com.minnolter.flowtrack.data.AppDatabase
import com.minnolter.flowtrack.data.ExpenseRepository
import com.minnolter.flowtrack.data.UserPreferencesRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database = AppDatabase.getDatabase(applicationContext)
            val repository = ExpenseRepository(database.expenseDao())
            val userPreferencesRepository = UserPreferencesRepository(applicationContext)
            val viewModelFactory = AppViewModelFactory(repository, userPreferencesRepository)

            val appViewModel: AppViewModel = viewModel(factory = viewModelFactory)
            val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

            FinanceAppTheme(isDarkTheme) {
                FinanceApp(appViewModel)
            }
        }
    }
}