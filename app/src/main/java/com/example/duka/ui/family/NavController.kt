package com.example.duka.ui.family

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            WelcomeScreen(navController = navController)
        }

        composable("family_dashboard") {
            FamilyDashboardScreen(navController = navController)
        }

        composable("create_family") {
            FamilyScreen(navController = navController)
        }
    }
}
