package com.example.duka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.duka.configs.Injection
import com.example.duka.data.model.ShoppingList
import com.example.duka.ui.theme.DukaTheme
import com.example.duka.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(
    familyId: Int,
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModel = viewModel(factory = Injection.provideViewModelFactory(context = LocalContext.current))
) {
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()

    shoppingListViewModel.loadShoppingLists(familyId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Grocery Lists") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_shopping_list/$familyId") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Shopping List")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(shoppingLists) { list ->
                ShoppingListCard(list)
            }
        }
    }
}

@Composable
fun ShoppingListCard(shoppingList: ShoppingList) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = shoppingList.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            shoppingList.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryListPreview() {
    DukaTheme {
        GroceryListScreen(familyId = 1, navController = rememberNavController())
    }
}
