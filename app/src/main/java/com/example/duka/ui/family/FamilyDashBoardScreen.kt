package com.example.duka.ui.family

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AccessTime


data class ShoppingList(
    val name: String,
    val totalItems: Int,
    val boughtItems: Int,
    val lastUpdated: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDashboardScreen(navController: NavController) {
    // Step 1: Sample Data
    val shoppingLists = remember {
        listOf(
            ShoppingList("Weekly Groceries", 20, 15, "Nov 2, 2025, 10:45 AM"),
            ShoppingList("Birthday Party", 10, 4, "Nov 1, 2025, 6:30 PM"),
            ShoppingList("Monthly Essentials", 30, 25, "Oct 30, 2025, 3:10 PM"),
            ShoppingList("Christmas Shopping", 50, 10, "Oct 28, 2025, 9:15 AM"),
            ShoppingList("School Supplies", 15, 12, "Oct 25, 2025, 5:40 PM"),
            ShoppingList("Weekend BBQ", 12, 8, "Oct 22, 2025, 7:20 PM"),
            ShoppingList("Home Cleaning", 18, 6, "Oct 18, 2025, 11:00 AM"),
            ShoppingList("Electronics Upgrade", 8, 2, "Oct 15, 2025, 4:50 PM"),
            ShoppingList("Baby Essentials", 25, 17, "Oct 10, 2025, 3:30 PM"),
            ShoppingList("Travel Packing", 14, 5, "Oct 5, 2025, 1:45 PM")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Family Dashboard") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createList") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add List")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(shoppingLists) { list ->
                ShoppingListCard(list)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ShoppingListCard(list: ShoppingList) {
    val progress = list.boughtItems.toFloat() / list.totalItems

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // 🔹 Header Row — name + item count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = list.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = "${list.boughtItems}/${list.totalItems}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            Spacer(Modifier.height(16.dp))

            // 🔹 Footer Row — icons for info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping List",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Shopping list",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Last updated",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = list.lastUpdated,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FamilyDashboardPreview() {
    val navController = rememberNavController()
    FamilyDashboardScreen(navController)
}
