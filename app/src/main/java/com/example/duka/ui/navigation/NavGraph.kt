package com.example.duka.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.duka.ui.family.FamilyScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "family") {
        composable("family") {
            FamilyScreen(
                onNavigateToHome = { /* Later */ },
                onJoinWithCode = { /* Later */ }
            )
        }
        // Add more screens later
    }
}