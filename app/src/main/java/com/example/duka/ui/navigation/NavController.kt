package com.example.duka.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.duka.data.database.DukaDatabase
import com.example.duka.ui.authentication.LoginScreen
import com.example.duka.ui.shoppingitems.AddShoppingListScreen
import com.example.duka.ui.family.FamilyDashboardScreen
import com.example.duka.ui.family.FamilyScreen
import com.example.duka.ui.family.FamilySettingsScreen
import com.example.duka.ui.family.WelcomeScreen
import com.example.duka.ui.screens.GroceryListScreen
import com.example.duka.ui.authentication.SignUpScreen



import com.example.duka.ui.shoppingitems.AddListItemScreen
import com.example.duka.ui.shoppingitems.AddShoppingListScreen
import com.example.duka.ui.shoppingitems.EditListItemScreen
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

        composable (route = "signup"){
            SignUpScreen(navController = navController)
        }

        composable (route = "login"){
            LoginScreen(navController = navController)
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

        composable(
            route = "add_list_item/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId")
            if (listId != null) {
                AddListItemScreen(
                    listId = listId,
                    navController = navController
                )
            }
        }

        // The new route for editing an item in a shopping list
        composable(
            route = "edit_list_item/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            if (itemId != null) {
                EditListItemScreen(
                    itemId = itemId,
                    navController = navController
                )
            }
        }
    }
}
