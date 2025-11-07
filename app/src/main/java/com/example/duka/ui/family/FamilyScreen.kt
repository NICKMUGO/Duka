//package com.example.duka.ui.family
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ChevronRight
//import androidx.compose.material.icons.filled.Group
//import androidx.compose.material.icons.filled.GroupAdd
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material3.AssistChip
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.duka.data.model.Family
//import com.example.duka.ui.theme.DukaTheme
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FamilyScreen(
//    viewModel: FamilyViewModel = viewModel(),
//    navController: NavController
//) {
//    val state by viewModel.uiState
//    val scope = rememberCoroutineScope()
//    val sheetState = rememberModalBottomSheetState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    var showBottomSheet by remember { mutableStateOf(false) }
//    var showCreateDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.snackbarMessage.collect { message ->
//            snackbarHostState.showSnackbar(message)
//        }
//    }
//
//    if (showBottomSheet) {
//        ModalBottomSheet(
//            onDismissRequest = { showBottomSheet = false },
//            sheetState = sheetState
//        ) {
//            ListItem(
//                headlineContent = { Text("Create a new family") },
//                leadingContent = { Icon(Icons.Default.GroupAdd, contentDescription = null) },
//                modifier = Modifier.clickable {
//                    scope.launch {
//                        sheetState.hide()
//                        showBottomSheet = false
//                        showCreateDialog = true
//                    }
//                }
//            )
//            ListItem(
//                headlineContent = { Text("Join with a code") },
//                leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
//                modifier = Modifier.clickable { /* TODO */ }
//            )
//            Spacer(Modifier.height(32.dp))
//        }
//    }
//
//    if (showCreateDialog) {
//        CreateFamilyDialog(
//            onCreate = {
//                viewModel.createFamily(it)
//                showCreateDialog = false
//            },
//            onDismiss = { showCreateDialog = false }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Family") }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { showBottomSheet = true }) {
//                Icon(Icons.Default.Add, contentDescription = "Create or join family")
//            }
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { padding ->
//        AnimatedContent(
//            targetState = state,
//            modifier = Modifier.padding(padding)
//        ) { screenState: FamilyScreenState ->
//            when (screenState) {
//                is FamilyScreenState.Loading -> {
//                    Box(Modifier.fillMaxSize(), Alignment.Center) {
//                        CircularProgressIndicator()
//                    }
//                }
//
//                is FamilyScreenState.NoFamily -> {
//                    CreateFamilyForm(
//                        onCreate = { viewModel.createFamily(it) },
//                        navController = navController
//                    )
//                }
//
//                is FamilyScreenState.HasFamilies -> {
//                    FamilyList(
//                        families = screenState.families,
//                        onSelect = {
//                            viewModel.selectFamily(it)
//                            navController.navigate("family_dashboard")
//                        },
//                        onManage = {
//                            navController.navigate("family_settings/${it.id}")
//                        },
//                    )
//                }
//
//                is FamilyScreenState.CreatingFamily -> {
//                    CreatingFamilyView(screenState)
//                }
//
//                is FamilyScreenState.Error -> {
//                    ErrorView(screenState.message)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CreateFamilyDialog(onCreate: (String) -> Unit, onDismiss: () -> Unit) {
//    var familyName by remember { mutableStateOf("") }
//
//    androidx.compose.material3.AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Create New Family") },
//        text = {
//            OutlinedTextField(
//                value = familyName,
//                onValueChange = { familyName = it },
//                label = { Text("Family Name") },
//                singleLine = true
//            )
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    if (familyName.isNotBlank()) {
//                        onCreate(familyName)
//                    }
//                },
//                enabled = familyName.isNotBlank()
//            ) {
//                Text("Create")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun CreateFamilyForm(onCreate: (String) -> Unit, navController: NavController) {
//    var familyName by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Icon(
//            Icons.Default.GroupAdd,
//            contentDescription = null,
//            modifier = Modifier.size(64.dp),
//            tint = MaterialTheme.colorScheme.primary
//        )
//        Spacer(Modifier.height(24.dp))
//        Text("No Family Found", style = MaterialTheme.typography.headlineMedium)
//        Spacer(Modifier.height(8.dp))
//        Text("Create one to get started", style = MaterialTheme.typography.bodyMedium)
//        Spacer(Modifier.height(32.dp))
//
//        OutlinedTextField(
//            value = familyName,
//            onValueChange = { familyName = it },
//            label = { Text("Family Name") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                onCreate(familyName)
//                familyName = ""
//            },
//            enabled = familyName.isNotBlank(),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Create Family")
//        }
//    }
//}
//
//@Composable
//fun FamilyList(families: List<Family>, onSelect: (Family) -> Unit, onManage: (Family) -> Unit) {
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(families) { family ->
//            FamilyCard(family = family, onSelect = onSelect, onManage = onManage)
//        }
//    }
//}
//
//@Composable
//fun FamilyCard(family: Family, onSelect: (Family) -> Unit, onManage: (Family) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { onSelect(family) },
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Group,
//                contentDescription = null,
//                modifier = Modifier.size(40.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )
//            Spacer(Modifier.width(16.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(family.name, style = MaterialTheme.typography.titleMedium)
//                Text("${family.memberCount} members", style = MaterialTheme.typography.bodySmall)
//                if (family.isOwner) {
//                    AssistChip(
//                        onClick = { },
//                        label = { Text("Owner") },
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
//                }
//            }
//            if (family.isOwner) {
//                IconButton(onClick = { onManage(family) }) {
//                    Icon(Icons.Filled.MoreVert, contentDescription = "Manage Family")
//                }
//            } else {
//                Icon(Icons.Default.ChevronRight, contentDescription = "Select")
//            }
//        }
//    }
//}
//
//@Composable
//fun CreatingFamilyView(state: FamilyScreenState.CreatingFamily) {
//    Box(Modifier.fillMaxSize(), Alignment.Center) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            CircularProgressIndicator()
//            Spacer(Modifier.height(16.dp))
//            Text("Creating family: ${state.name}...")
//        }
//    }
//}
//
//@Composable
//fun ErrorView(message: String) {
//    Box(Modifier.fillMaxSize(), Alignment.Center) {
//        Text("Error: $message", color = MaterialTheme.colorScheme.error)
//    }
//}
//
//// --- PREVIEWS ---
//
//@Preview(showBackground = true, name = "No Family")
//@Composable
//fun PreviewNoFamily() {
//    val fakeNavController = rememberNavController()
//    DukaTheme {
//        CreateFamilyForm(onCreate = {}, navController = fakeNavController)
//    }
//}
//
//@Preview(showBackground = true, name = "Has Families")
//@Composable
//fun PreviewHasFamilies() {
//    DukaTheme {
//        FamilyList(
//            families = listOf(
//                Family("1", "The Johnson's", 4, true),
//                Family("2", "Weekend Crew", 6, false)
//            ),
//            onSelect = {},
//            onManage = {}
//        )
//    }
//}
