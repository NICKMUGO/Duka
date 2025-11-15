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

class FamilyViewModel(
    private val repository: FamilyRepository,
    private val currentUserId: Int
) : ViewModel() {

    private val _uiState = mutableStateOf<FamilyScreenState>(FamilyScreenState.Loading)
    val uiState: State<FamilyScreenState> = _uiState

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        loadFamilies()
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
            _uiState.value = FamilyScreenState.CreatingFamily(name)
            try {
                repository.createFamily(name, currentUserId)
                _snackbarMessage.emit("Family '$name' created")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error creating family: ${e.message}")
            } finally {
                loadFamilies()
            }
        }
    }

    fun updateFamilyName(familyId: Int, newName: String) {
        viewModelScope.launch {
            try {
                repository.updateFamilyName(familyId, newName)
                _snackbarMessage.emit("Family name updated")
                loadFamilies() // Reload to show the change
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
                loadFamilies()
            } catch (e: Exception) {
                _snackbarMessage.emit("Error leaving family: ${e.message}")
            }
        }
    }
}