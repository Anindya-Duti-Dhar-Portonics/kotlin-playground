package com.example.kotlin_basic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kotlin_basic.data.model.User
import com.example.kotlin_basic.ui.state.UiState
import com.example.kotlin_basic.ui.viewmodel.UserViewModel
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit,
    onUserClick: (String) -> Unit
) {
    // StateFlow Collect করা
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    // rememberSaveable: configuration change হলেও search text ধরে রাখে
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    // remember: এই Composition-এর মধ্যে email visibility state ধরে রাখে
    var showEmail by remember {
        mutableStateOf(true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("User List")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(text = "Error: ${state.message}")
                is UiState.Success -> {
                    val filteredUsers = state.users.filter { user ->
                        user.name.contains(searchQuery, ignoreCase = true) ||
                            user.email.contains(searchQuery, ignoreCase = true)
                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search users") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Show email")
                            Switch(
                                checked = showEmail,
                                onCheckedChange = { showEmail = it }
                            )
                        }

                        UserList(
                            users = filteredUsers,
                            showEmail = showEmail,
                            onUserClick = onUserClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserList(
    users: List<User>,
    showEmail: Boolean,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserCard(
                user = user,
                showEmail = showEmail,
                onClick = {
                    onUserClick(user.id)
                }
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    showEmail: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                if (showEmail) {
                    Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
