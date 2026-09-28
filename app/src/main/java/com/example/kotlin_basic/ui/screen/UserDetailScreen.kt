package com.example.kotlin_basic.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kotlin_basic.ui.state.UiState
import com.example.kotlin_basic.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: String,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val uiState by userViewModel.uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("User Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        when (val state = uiState) {
            UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is UiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is UiState.Success -> {
                val user = state.users
                    .find { it.id == userId }

                if (user != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = user.avatar,
                            contentDescription = "Profile picture of ${user.name}",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )

                        Text(text = "ID: ${user.id}")
                        Text(text = "Name: ${user.name}")
                        Text(text = "Email: ${user.email}")
                    }
                } else {
                    Text(
                        text = "User not found",
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}
