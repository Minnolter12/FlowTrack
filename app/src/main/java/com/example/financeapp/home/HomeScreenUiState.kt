package com.example.financeapp.home

sealed class HomeScreenUiState {
    data class Result(
        val total: Int
    ) : HomeScreenUiState()

    data class Error(
        val message: String? = null
    ) : HomeScreenUiState()
}