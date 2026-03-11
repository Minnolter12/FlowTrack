package com.minnolter.flowtrack.app

import com.minnolter.flowtrack.data.ExpenseEntity
import com.minnolter.flowtrack.data.ExpenseRepository
import com.minnolter.flowtrack.data.UserPreferencesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minnolter.flowtrack.data.ExpenseItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector

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

    fun addItem(name: String, category: String, icon: String) {
        viewModelScope.launch {
            repository.insertItem(
                ExpenseItemEntity(
                    title = name,
                    category = category,
                    icon = icon 
                )
            )
        }
    }

    fun getIconByName(name: String): ImageVector {
        return when (name) {
            Icons.Rounded.Add.name -> Icons.Rounded.Add
            Icons.Rounded.Restaurant.name -> Icons.Rounded.Restaurant
            Icons.Rounded.ShoppingCart.name -> Icons.Rounded.ShoppingCart
            Icons.Rounded.DirectionsCar.name -> Icons.Rounded.DirectionsCar
            Icons.Rounded.Home.name -> Icons.Rounded.Home
            Icons.Rounded.Bolt.name -> Icons.Rounded.Bolt
            Icons.Rounded.Movie.name -> Icons.Rounded.Movie
            Icons.Rounded.Store.name -> Icons.Rounded.Store
            Icons.Rounded.MedicalServices.name -> Icons.Rounded.MedicalServices
            Icons.Rounded.Payments.name -> Icons.Rounded.Payments
            Icons.Rounded.School.name -> Icons.Rounded.School
            Icons.Rounded.Pets.name -> Icons.Rounded.Pets
            Icons.Rounded.Subscriptions.name -> Icons.Rounded.Subscriptions
            Icons.Rounded.Redeem.name -> Icons.Rounded.Redeem
            Icons.Rounded.Flight.name -> Icons.Rounded.Flight
            Icons.Rounded.FitnessCenter.name -> Icons.Rounded.FitnessCenter
            Icons.Rounded.LocalCafe.name -> Icons.Rounded.LocalCafe
            Icons.Rounded.DirectionsBus.name -> Icons.Rounded.DirectionsBus
            Icons.Rounded.SelfImprovement.name -> Icons.Rounded.SelfImprovement
            Icons.Rounded.VideogameAsset.name -> Icons.Rounded.VideogameAsset
            else -> Icons.Rounded.Add
        }
    }

    fun deleteItem(item: ExpenseItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun updateCategoryIcon(categoryName: String, newIcon: String) {
        viewModelScope.launch {
            repository.updateCategoryIcon(categoryName, newIcon)
        }
    }
}
