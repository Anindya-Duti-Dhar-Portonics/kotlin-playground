package com.example.kotlin_basic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_basic.data.remote.RetrofitInstance
import com.example.kotlin_basic.repository.UserRepository
import com.example.kotlin_basic.ui.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class UserViewModel(
    private val repository: UserRepository = UserRepository(RetrofitInstance.api)
) : ViewModel() {

    // Hot Flow (StateFlow): give immidiate update to UI
    // use stateIn to collect Flow (stores the latest emitted value)
    val uiState: StateFlow<UiState> = repository.getUsersFlow()
        .stateIn(
            scope = viewModelScope, // if view model cancel then the work will not continue
            started = SharingStarted.WhileSubscribed(5000), // Lifecycle safe handling
            initialValue = UiState.Loading
        )

    companion object {
        const val TAG = "UserViewModel"
    }
}
