package com.example.duka.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.duka.ui.family.FamilyScreen
import com.example.duka.ui.family.FamilySettingsScreen
import com.example.duka.ui.family.WelcomeScreen
import com.example.duka.ui.screens.GroceryListScreen
import com.example.duka.ui.shoppingitems.AddShoppingListScreen
import com.example.duka.ui.shoppingitems.EditShoppingListScreen
import com.example.duka.ui.shoppingitems.ListItemScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            WelcomeScreen(navController = navController)
        }

        composable("family_hub") {
            FamilyScreen(navController = navController)
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

        composable(
            route = "edit_shopping_list/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId")
            if (listId != null) {
                EditShoppingListScreen(
                    navController = navController,
                    listId = listId
                )
            }
        }

        composable(
            route = "family_settings/{familyId}",
            arguments = listOf(navArgument("familyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getInt("familyId")
            if (familyId != null) {
                FamilySettingsScreen(
                    familyId = familyId,
                    navController = navController
                )
            }
        }

        // The new route for viewing items in a shopping list
        composable(
            route = "list_items/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId")
            if (listId != null) {
                ListItemScreen(
                    listId = listId,
                    navController = navController
                )
            }
        }
    }
}
