package com.example.duka.ui.family


import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
// ADD THESE LINES
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.ChevronRight



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // CHANGED: Added this import
import com.example.duka.data.model.Family
import com.example.duka.ui.theme.DukaTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    // CHANGED: Replaced FamilyViewModel() with the viewModel() composable function.
    // This correctly scopes the ViewModel to the navigation destination,
    // so it survives recomposition and retains its state.
    viewModel: FamilyViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onJoinWithCode: () -> Unit
) {
    val state by viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Family") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onJoinWithCode) {
                Icon(Icons.Default.Add, "Join with Code")
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = state,
            modifier = Modifier.padding(padding)
        ) { screenState: FamilyScreenState ->
            when (screenState) {
                is FamilyScreenState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is FamilyScreenState.NoFamily -> {
                    CreateFamilyForm(
                        onCreate = { viewModel.createFamily(it) }
                    )
                }
                is FamilyScreenState.HasFamilies -> {
                    FamilyList(
                        families = screenState.families,
                        onSelect = {
                            viewModel.selectFamily(it)
                            onNavigateToHome()
                        }
                    )
                }
                is FamilyScreenState.CreatingFamily -> {
                    CreatingFamilyView(screenState)
                }
                is FamilyScreenState.Error -> {
                    ErrorView(screenState.message)
                }
            }
        }
    }
}


// --- NO OTHER CHANGES ARE NEEDED BELOW THIS LINE ---

@Composable
fun CreateFamilyForm(onCreate: (String) -> Unit) {
    var familyName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.GroupAdd, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))
        Text("No Family Found", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Create one to get started", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = familyName,
            onValueChange = { familyName = it },
            label = { Text("Family Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                onCreate(familyName)
                familyName = ""
            },
            enabled = familyName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Family")
        }
    }
}

@Composable
fun FamilyList(families: List<Family>, onSelect: (Family) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(families) { family ->
            FamilyCard(family = family, onSelect = onSelect)
        }
    }
}

@Composable
fun FamilyCard(family: Family, onSelect: (Family) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelect(family) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(family.name, style = MaterialTheme.typography.titleMedium)
                Text("${family.memberCount} members", style = MaterialTheme.typography.bodySmall)
                if (family.isOwner) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Owner") },
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, "Select")
        }
    }
}

@Composable
fun CreatingFamilyView(state: FamilyScreenState.CreatingFamily) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Creating family: ${state.name}...")
        }
    }
}

@Composable
fun ErrorView(message: String) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Preview(showBackground = true, name = "No Family")
@Composable
fun PreviewNoFamily() {
    DukaTheme {
        CreateFamilyForm(onCreate = {})
    }
}

@Preview(showBackground = true, name = "Has Families")
@Composable
fun PreviewHasFamilies() {
    DukaTheme {
        FamilyList(
            families = listOf(
                Family("1", "The Johnsons", 4, true),
                Family("2", "Weekend Crew", 6, false)
            ),
            onSelect = {}
        )
    }
}