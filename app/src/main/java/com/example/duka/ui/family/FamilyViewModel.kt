package com.example.duka.ui.family

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.repository.FamilyDetails
import com.example.duka.data.repository.FamilyRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

// NEW: Dedicated state for the FamilySettingsScreen
sealed class FamilySettingsState {
    object Loading : FamilySettingsState()
    data class Success(val familyDetails: FamilyDetails) : FamilySettingsState()
    data class Error(val message: String) : FamilySettingsState()
}

class FamilyViewModel(
    private val repository: FamilyRepository,
    private val currentUserId: Int
) : ViewModel() {

    private val _uiState = mutableStateOf<FamilyScreenState>(FamilyScreenState.Loading)
    val uiState: State<FamilyScreenState> = _uiState

    // NEW: State holder for the settings screen
    private val _settingsState = mutableStateOf<FamilySettingsState>(FamilySettingsState.Loading)
    val settingsState: State<FamilySettingsState> = _settingsState

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        loadFamilies()
    }

    // NEW: Dedicated loader for the settings screen
    fun loadFamilyDetails(familyId: Int) {
        viewModelScope.launch {
            _settingsState.value = FamilySettingsState.Loading
            try {
                // We find the specific family for the current user.
                val familyDetails = repository.getFamilyDetailsForUser(currentUserId)
                    .find { it.family.id == familyId }

                if (familyDetails != null) {
                    _settingsState.value = FamilySettingsState.Success(familyDetails)
                } else {
                    _settingsState.value = FamilySettingsState.Error("Family not found or you are not a member.")
                    _snackbarMessage.emit("Could not find family details.")
                }
            } catch (e: Exception) {
                _settingsState.value = FamilySettingsState.Error("Failed to load details: ${e.message}")
                _snackbarMessage.emit("Error: ${e.message}")
            }
        }
    }

    fun loadFamilies() {
        viewModelScope.launch {
            _uiState.value = FamilyScreenState.Loading
            try {
                val families = repository.getFamilyDetailsForUser(currentUserId)
                _uiState.value = if (families.isEmpty()) {
                    FamilyScreenState.NoFamily()
                } else {
                    FamilyScreenState.HasFamilies(families)
                }
            } catch (e: Exception) {
                _uiState.value = FamilyScreenState.Error("Failed to load families: ${e.message}")
            }
        }
    }

    fun createFamily(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            // This state change affects the main screen, which is correct
            _uiState.value = FamilyScreenState.CreatingFamily(name)
            try {
                repository.createFamily(name, currentUserId)
                _snackbarMessage.emit("Family '$name' created")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error creating family: ${e.message}")
            } finally {
                // Reload the main list
                loadFamilies()
            }
        }
    }

    fun updateFamilyName(familyId: Int, newName: String) {
        viewModelScope.launch {
            try {
                repository.updateFamilyName(familyId, newName)
                _snackbarMessage.emit("Family name updated")
                // After updating, reload both the main list and the specific details
                loadFamilies() // Reloads main screen for when user navigates back
                loadFamilyDetails(familyId) // Reloads settings screen to show change immediately
            } catch (e: Exception) {
                _snackbarMessage.emit("Error updating name: ${e.message}")
            }
        }
    }

    fun deleteFamily(familyId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteFamily(familyId)
                _snackbarMessage.emit("Family deleted")
                // No need to reload details for a deleted family, just the main list
                loadFamilies()
            } catch (e: Exception) {
                _snackbarMessage.emit("Error deleting family: ${e.message}")
            }
        }
    }

    fun leaveFamily(familyId: Int) {
        viewModelScope.launch {
            try {
                repository.leaveFamily(familyId, currentUserId)
                _snackbarMessage.emit("You have left the family")
                // No need to reload details for a family you've left, just the main list
                loadFamilies()
            } catch (e: Exception) {
                _snackbarMessage.emit("Error leaving family: ${e.message}")
            }
        }
    }
}
