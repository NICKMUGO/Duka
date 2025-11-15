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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.duka.configs.Injection
import com.example.duka.data.model.Family
import com.example.duka.data.repository.FamilyDetails
import com.example.duka.ui.theme.DukaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilySettingsScreen(
    familyId: Int,
    viewModel: FamilyViewModel = viewModel(factory = Injection.provideViewModelFactory(LocalContext.current)),
    navController: NavController
) {
    val state by viewModel.uiState

    val familyDetails = remember(state, familyId) {
        (state as? FamilyScreenState.HasFamilies)?.families?.find { it.family.id == familyId }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Family Settings") })
        }
    ) { padding ->
        if (familyDetails != null) {
            FamilySettingsContent(
                modifier = Modifier.padding(padding),
                familyDetails = familyDetails,
                onSave = { newName ->
                    viewModel.updateFamilyName(familyDetails.family.id, newName)
                },
                onDelete = {
                    viewModel.deleteFamily(familyDetails.family.id)
                    navController.popBackStack()
                },
                onLeave = {
                    viewModel.leaveFamily(familyDetails.family.id)
                    navController.popBackStack()
                }
            )
        } else {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun FamilySettingsContent(
    modifier: Modifier = Modifier,
    familyDetails: FamilyDetails,
    onSave: (String) -> Unit,
    onDelete: () -> Unit,
    onLeave: () -> Unit
) {
    var familyName by remember { mutableStateOf(familyDetails.family.name) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(familyDetails.family.name) {
        familyName = familyDetails.family.name
    }

    if (showDialog) {
        ConfirmationDialog(
            isOwner = familyDetails.isOwner,
            onConfirm = {
                if (familyDetails.isOwner) onDelete() else onLeave()
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
            readOnly = !familyDetails.isOwner
        )
        Spacer(Modifier.height(16.dp))
        if (familyDetails.isOwner) {
            Button(
                onClick = { onSave(familyName) },
                enabled = familyName.isNotBlank() && familyName != familyDetails.family.name,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (familyDetails.isOwner) "Delete Family" else "Leave Family")
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
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
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
            familyDetails = FamilyDetails(Family(id=1, name="The Preview Family", memberCount=4), isOwner = true),
            onSave = {}, onDelete = {}, onLeave = {}
        )
    }
}

@Preview(showBackground = true, name = "Member View")
@Composable
fun FamilySettingsScreenMemberPreview() {
    DukaTheme {
        FamilySettingsContent(
            familyDetails = FamilyDetails(Family(id=1, name="Vacation Crew", memberCount=8), isOwner = false),
            onSave = {}, onDelete = {}, onLeave = {}
        )
    }
}
