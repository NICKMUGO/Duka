package com.example.duka.ui.family

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.duka.Injection
import com.example.duka.data.repository.FamilyDetails
import com.example.duka.ui.theme.DukaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    navController: NavController,
    viewModel: FamilyViewModel = viewModel(factory = Injection.provideViewModelFactory(LocalContext.current))
) {
    val state by viewModel.uiState
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            ListItem(
                headlineContent = { Text("Create a new family") },
                leadingContent = { Icon(Icons.Default.GroupAdd, contentDescription = null) },
                modifier = Modifier.clickable {
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
                        showCreateDialog = true
                    }
                }
            )
            ListItem(
                headlineContent = { Text("Join with a code") },
                leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
                modifier = Modifier.clickable { /* TODO */ }
            )
            Spacer(Modifier.height(32.dp))
        }
    }

    if (showCreateDialog) {
        CreateFamilyDialog(
            onCreate = {
                viewModel.createFamily(it)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Family") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Create or join family")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        AnimatedContent(
            targetState = state,
            modifier = Modifier.padding(padding),
            label = "FamilyScreenAnimation"
        ) { screenState ->
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
                        onSelect = { /* viewModel.selectFamily(it) - TODO */ navController.navigate("shopping_lists") },
                        onManage = { navController.navigate("family_settings/${it.family.id}") },
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

@Composable
fun CreateFamilyDialog(onCreate: (String) -> Unit, onDismiss: () -> Unit) {
    var familyName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Family") },
        text = {
            OutlinedTextField(
                value = familyName,
                onValueChange = { familyName = it },
                label = { Text("Family Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (familyName.isNotBlank()) {
                        onCreate(familyName)
                    }
                },
                enabled = familyName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

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
        Icon(
            Icons.Default.GroupAdd,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
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
fun FamilyList(families: List<FamilyDetails>, onSelect: (FamilyDetails) -> Unit, onManage: (FamilyDetails) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(families) { familyDetails ->
            FamilyCard(familyDetails = familyDetails, onSelect = onSelect, onManage = onManage)
        }
    }
}

@Composable
fun FamilyCard(familyDetails: FamilyDetails, onSelect: (FamilyDetails) -> Unit, onManage: (FamilyDetails) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSelect(familyDetails) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Text(familyDetails.family.name, style = MaterialTheme.typography.titleMedium)
                Text("${familyDetails.family.memberCount} members", style = MaterialTheme.typography.bodySmall)
                if (familyDetails.isOwner) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Owner") },
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            if (familyDetails.isOwner) {
                IconButton(onClick = { onManage(familyDetails) }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Manage Family")
                }
            } else {
                Icon(Icons.Default.ChevronRight, contentDescription = "Select")
            }
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

@Preview(showBackground = true)
@Composable
fun FamilyScreenPreview() {
    DukaTheme {
        // This preview will show the loading state initially.
        // More complex previews can be built to show specific states.
        FamilyScreen(navController = NavController(LocalContext.current))
    }
}
