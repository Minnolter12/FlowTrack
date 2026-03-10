package com.minnolter.flowtrack.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow;

class HomeScreenViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Result(0))
    val uiState = _uiState.asStateFlow()

}
