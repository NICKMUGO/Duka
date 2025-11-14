package com.example.duka.ui.family

import com.example.duka.data.repository.FamilyDetails

// Defines the different states the FamilyScreen can be in.
sealed interface FamilyScreenState {
    object Loading : FamilyScreenState
    data class NoFamily(val message: String = "No families found. Create one to get started!") : FamilyScreenState
    data class HasFamilies(val families: List<FamilyDetails>) : FamilyScreenState
    data class CreatingFamily(val name: String) : FamilyScreenState
    data class Error(val message: String) : FamilyScreenState
}