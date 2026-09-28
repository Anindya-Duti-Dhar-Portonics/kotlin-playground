package com.example.kotlin_basic.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlin_basic.ui.screen.HomeScreen
import com.example.kotlin_basic.ui.screen.UserDetailScreen
import com.example.kotlin_basic.ui.screen.UserListScreen
import com.example.kotlin_basic.ui.viewmodel.UserViewModel

@Composable
fun AppNavigation(
    userViewModel: UserViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onOpenUsers = {
                    navController.navigate("users")
                }
            )
        }

        composable("users") {
            UserListScreen(
                userViewModel = userViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onUserClick = { userId ->
                    navController.navigate("user/$userId")
                }
            )
        }

        composable(
            route = "user/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId")

            if (userId != null) {
                UserDetailScreen(
                    userId = userId,
                    userViewModel = userViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}