package com.example.duka.ui.family

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.duka.ui.screens.GroceryListScreen


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome" // We start at the family screen for this flow
    ) {

        composable("family_dashboard") {
            FamilyDashboardScreen(navController = navController)
        }

        composable("welcome") {
            WelcomeScreen(navController = navController)
        }

        composable(
            route = "grocery_list/{familyId}",
            arguments = listOf(navArgument("familyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getInt("familyId")
            if (familyId != null) {
                GroceryListScreen(
                    familyId = familyId,
                    navController = navController
                )
            }
        }

        composable(
            route = "add_shopping_list/{familyId}",
            arguments = listOf(navArgument("familyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getInt("familyId")
            if (familyId != null) {
                AddShoppingListScreen(
                    navController = navController,
                    familyId = familyId
                )
            }
        }
    }
}
