package com.example.duka.ui.shoppingitems

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun AddShoppingListScreen(
    navController: NavController,
    familyId: Int,
    shoppingListViewModel: ShoppingListViewModel = viewModel(factory = Injection.provideViewModelFactory(context = LocalContext.current))
) {
    var listName by remember { mutableStateOf("") }
    var listDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Shopping List") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text("List Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = listDescription,
                onValueChange = { listDescription = it },
                label = { Text("List Description (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                val newList = ShoppingList(
                    familyId = familyId,
                    name = listName,
                    description = listDescription.takeIf { it.isNotBlank() },
                    createdBy = 1 // Hardcoded user ID for now
                )
                shoppingListViewModel.addShoppingList(newList)
                navController.popBackStack()
            }) {
                Text("Save List")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddShoppingListScreenPreview() {
    DukaTheme {
        AddShoppingListScreen(
            navController = rememberNavController(),
            familyId = 1
        )
    }
}
