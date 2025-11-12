package com.example.duka.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duka.R
import com.example.duka.ui.theme.DukaTheme

data class GroceryItem(
    val name: String,
    val quantity: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen() {
    val groceryItems = listOf(
        GroceryItem("Tomatoes", "1 kg", R.drawable.ic_launcher_foreground),
        GroceryItem("Milk", "2 packets", R.drawable.ic_launcher_foreground),
        GroceryItem("Bread", "1 loaf", R.drawable.ic_launcher_foreground)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Grocery List") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(groceryItems) { item ->
                GroceryItemCard(item)
            }
        }
    }
}

@Composable
fun GroceryItemCard(item: GroceryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = item.quantity,
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
        GroceryListScreen()
    }
}
