package com.example.duka.ui.family

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.duka.data.model.Family
import com.example.duka.ui.theme.DukaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilySettingsScreen(
    familyId: String,
    viewModel: FamilyViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.uiState

    val family = remember(state, familyId) {
        (state as? FamilyScreenState.HasFamilies)?.families?.find { it.id == familyId }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Family Settings") })
        }
    ) { padding ->
        if (family != null) {
            FamilySettingsContent(
                modifier = Modifier.padding(padding),
                family = family,
                onSave = { newName ->
                    viewModel.updateFamilyName(family.id, newName)
                    navController.popBackStack()
                },
                onDelete = {
                    viewModel.deleteFamily(family.id)
                    navController.popBackStack()
                },
                onLeave = {
                    viewModel.leaveFamily(family.id)
                    navController.popBackStack()
                }
            )
        } else {
            // Family not found, maybe navigate back or show an error
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun FamilySettingsContent(
    modifier: Modifier = Modifier,
    family: Family,
    onSave: (String) -> Unit,
    onDelete: () -> Unit,
    onLeave: () -> Unit
) {
    var familyName by remember { mutableStateOf(family.name) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(family.name) {
        familyName = family.name
    }

    if (showDialog) {
        ConfirmationDialog(
            isOwner = family.isOwner,
            onConfirm = {
                if (family.isOwner) onDelete() else onLeave()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = familyName,
            onValueChange = { familyName = it },
            label = { Text("Family Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = !family.isOwner // Only owners can edit the name
        )
        Spacer(Modifier.height(16.dp))
        if (family.isOwner) {
            Button(
                onClick = { onSave(familyName) },
                enabled = familyName.isNotBlank() && familyName != family.name,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text("Save Changes")
            }
        }

        Spacer(Modifier.height(32.dp))

        // Destructive action button
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (family.isOwner) "Delete Family" else "Leave Family")
        }
    }
}

@Composable
fun ConfirmationDialog(
    isOwner: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (isOwner) "Delete Family?" else "Leave Family?"
    val text = if (isOwner) {
        "Are you sure you want to permanently delete this family? This action cannot be undone."
    } else {
        "Are you sure you want to leave this family?"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, name = "Owner View")
@Composable
fun FamilySettingsScreenOwnerPreview() {
    DukaTheme {
        FamilySettingsContent(
            family = Family("preview_id", "The Preview Family", 4, true),
            onSave = {},
            onDelete = {},
            onLeave = {}
        )
    }
}

@Preview(showBackground = true, name = "Member View")
@Composable
fun FamilySettingsScreenMemberPreview() {
    DukaTheme {
        FamilySettingsContent(
            family = Family("preview_id", "Vacation Crew", 8, false),
            onSave = {},
            onDelete = {},
            onLeave = {}
        )
    }
}
