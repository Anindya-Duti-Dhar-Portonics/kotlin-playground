package com.example.kotlin_basic.ui.state

import com.example.kotlin_basic.data.model.User

sealed interface UiState {
    data object Loading : UiState
    data class Success(val users: List<User>) : UiState
    data class Error(val message: String) : UiState
}