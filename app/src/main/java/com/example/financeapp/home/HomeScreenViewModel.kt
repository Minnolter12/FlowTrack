package com.example.financeapp.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow;
import java.time.LocalTime

class HomeScreenViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Result(0))
    val uiState = _uiState.asStateFlow()

}
