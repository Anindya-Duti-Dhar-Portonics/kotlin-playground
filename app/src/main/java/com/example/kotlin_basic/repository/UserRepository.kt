package com.example.kotlin_basic.repository

import com.example.kotlin_basic.data.model.User
import com.example.kotlin_basic.data.remote.ApiService
import com.example.kotlin_basic.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val apiService: ApiService) {

    // Cold Flow: Data Fetching and Data Processing
    // using flow as we need pipeline (otherwise use launch / withContext)
    fun getUsersFlow(): Flow<UiState> = flow {
        emit(UiState.Loading)
        try {
            val rawList: List<User> = apiService.getUsers()

            // Sequence (lazy operation and don't create list as soon as (create only after process everything and toList())), Map, for-loop step
            val processedUsers = rawList.asSequence()
                .filter { it.name.isNotBlank() } // remove empty name
                .distinctBy { it.id }            // filter by unique ID
                .toList()

            emit(UiState.Success(processedUsers))
        } catch (e: Exception) {
            emit(UiState.Error(e.localizedMessage ?: "Network error!"))
        }
    }.flowOn(Dispatchers.IO) // flowOn: when a flow need background process I/O Dispatcher selection
        // .flowOn(Dispatchers.IO)        // network, database, files
        // .flowOn(Dispatchers.Default)   // heavy calculations
        // .flowOn(Dispatchers.Main)      // usually UI work
}
