package com.example.duka.ui.family

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.model.Family
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeFamilyRepository {
    private val families = mutableListOf<Family>()

    suspend fun getUserFamilies(): List<Family> {
        delay(500)
        // Add some default data for testing if the list is empty
        if (families.isEmpty()) {
            families.add(Family("fam_1", "The Millers", 4, true))
            families.add(Family("fam_2", "Summer Vacation Crew", 8, false))
        }
        return families.toList()
    }

    suspend fun saveFamily(family: Family) {
        delay(500)
        families.add(family)
    }

    suspend fun updateFamily(updatedFamily: Family) {
        delay(500)
        val index = families.indexOfFirst { it.id == updatedFamily.id }
        if (index != -1) {
            families[index] = updatedFamily
        }
    }

    suspend fun deleteFamily(familyId: String) {
        delay(500)
        families.removeAll { it.id == familyId }
    }

    suspend fun leaveFamily(familyId: String) {
        delay(500)
        // In a real app, this would involve more complex logic,
        // like checking if the user is the last owner.
        families.removeAll { it.id == familyId }
    }

    suspend fun setActiveFamily(family: Family) {
        // Save to SharedPreferences later
    }
}

class FamilyViewModel : ViewModel() {

    private val repository = FakeFamilyRepository()

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
            val families = repository.getUserFamilies()
            _uiState.value = when {
                families.isEmpty() -> FamilyScreenState.NoFamily()
                else -> FamilyScreenState.HasFamilies(families)
            }
        }
    }

    fun createFamily(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            _uiState.value = FamilyScreenState.CreatingFamily(name)
            delay(800)
            val newFamily = Family(
                id = "fam_${System.currentTimeMillis()}",
                name = name,
                isOwner = true
            )
            repository.saveFamily(newFamily)
            _snackbarMessage.emit("Family '$name' created")
            loadFamilies()
        }
    }

    fun selectFamily(family: Family) {
        viewModelScope.launch {
            repository.setActiveFamily(family)
            // In real app: navigate to home
        }
    }

    fun updateFamilyName(familyId: String, newName: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is FamilyScreenState.HasFamilies) {
                val familyToUpdate = currentState.families.find { it.id == familyId }
                // Ensure the user is the owner and the name has actually changed
                if (familyToUpdate != null && familyToUpdate.isOwner && familyToUpdate.name != newName) {
                    val updatedFamily = familyToUpdate.copy(name = newName)
                    repository.updateFamily(updatedFamily)
                    _snackbarMessage.emit("Family name updated")
                    
                    // Update the UI state optimistically to avoid a full reload
                    val updatedList = currentState.families.map {
                        if (it.id == familyId) updatedFamily else it
                    }
                    _uiState.value = currentState.copy(families = updatedList)
                }
            }
        }
    }

    fun deleteFamily(familyId: String) {
        viewModelScope.launch {
            val familyName = (uiState.value as? FamilyScreenState.HasFamilies)?.families?.find { it.id == familyId }?.name
            repository.deleteFamily(familyId)
            if (familyName != null) {
                _snackbarMessage.emit("Family '$familyName' deleted")
            }
            loadFamilies() // Reload the list to reflect the change
        }
    }

    fun leaveFamily(familyId: String) {
        viewModelScope.launch {
            val familyName = (uiState.value as? FamilyScreenState.HasFamilies)?.families?.find { it.id == familyId }?.name
            repository.leaveFamily(familyId)
            if (familyName != null) {
                _snackbarMessage.emit("You have left '$familyName'")
            }
            loadFamilies() // Reload the list to reflect the change
        }
    }
}
