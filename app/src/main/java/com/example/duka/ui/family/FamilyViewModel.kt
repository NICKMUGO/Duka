package com.example.duka.ui.family

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.model.Family
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class FakeFamilyRepository {
    private val families = mutableListOf<Family>()

    suspend fun getUserFamilies(): List<Family> {
        delay(500)
        return families.toList()
    }

    suspend fun saveFamily(family: Family) {
        families.add(family)
    }

    suspend fun setActiveFamily(family: Family) {
        // Save to SharedPreferences later
    }
}

class FamilyViewModel : ViewModel() {

    private val repository = FakeFamilyRepository()

    private val _uiState = mutableStateOf<FamilyScreenState>(FamilyScreenState.Loading)
    val uiState: State<FamilyScreenState> = _uiState

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
            loadFamilies()
        }
    }

    fun selectFamily(family: Family) {
        viewModelScope.launch {
            repository.setActiveFamily(family)
            // In real app: navigate to home
        }
    }
}