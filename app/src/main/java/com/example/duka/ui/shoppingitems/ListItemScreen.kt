package com.example.duka.ui.shoppingitems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.duka.configs.Injection
import com.example.duka.data.model.ListItem
import com.example.duka.viewmodel.ListItemViewModel
import com.example.duka.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemScreen(
    listId: Int,
    navController: NavController,
    listItemViewModel: ListItemViewModel = viewModel(factory = Injection.provideViewModelFactory(context = LocalContext.current)),
    shoppingListViewModel: ShoppingListViewModel = viewModel(factory = Injection.provideViewModelFactory(context = LocalContext.current))
) {
    val listItems by listItemViewModel.listItems.collectAsState()
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()

    val listName = remember(shoppingLists, listId) {
        shoppingLists.find { it.id == listId }?.name ?: "Shopping Items"
    }

    listItemViewModel.loadListItems(listId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(listName) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_list_item/$listId") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listItems) { item ->
                ListItemCard(
                    item = item,
                    onItemBoughtChanged = {
                        listItemViewModel.markItemAsBought(item.id, it)
                    },
                    onEdit = {
                        navController.navigate("edit_list_item/${item.id}")
                    },
                    onDelete = {
                        listItemViewModel.deleteListItem(item)
                    }
                )
            }
        }
    }
}

@Composable
fun ListItemCard(
    item: ListItem,
    onItemBoughtChanged: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isBought,
                onCheckedChange = onItemBoughtChanged
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = item.name,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else null,
                    color = if (item.isBought) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                )
                item.quantity?.let {
                    Text(
                        text = "Qty: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.isBought) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Manage Item")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = {
                        onEdit()
                        showMenu = false
                    })
                    DropdownMenuItem(text = { Text("Delete") }, onClick = {
                        onDelete()
                        showMenu = false
                    })
                }
            }
        }
    }
}
