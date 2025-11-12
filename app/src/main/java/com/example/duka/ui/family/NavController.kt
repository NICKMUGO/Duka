package com.example.duka.ui.family

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome" // We start at the family screen for this flow
    ) {
//        composable("family") {
//            FamilyScreen(navController = navController)
//        }
//
//        composable(
//            route = "family_settings/{familyId}",
//            arguments = listOf(navArgument("familyId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val familyId = backStackEntry.arguments?.getString("familyId")
//            if (familyId != null) {
//                FamilySettingsScreen(
//                    familyId = familyId,
//                    navController = navController
//                )
//            }
//        }

        composable("family_dashboard") {
            FamilyDashboardScreen(navController = navController)
        }

        // The WelcomeScreen is not used in this flow, but I'll keep it here for now
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
    }
}
