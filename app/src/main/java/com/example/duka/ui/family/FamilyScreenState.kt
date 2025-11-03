package com.example.duka.ui.family

import com.example.duka.data.model.Family

sealed class FamilyScreenState {
    object Loading : FamilyScreenState()
    data class NoFamily(val inviteCode: String? = null) : FamilyScreenState()
    data class HasFamilies(
        val families: List<Family>,
        val selectedFamily: Family? = null
    ) : FamilyScreenState()
    data class CreatingFamily(val name: String = "") : FamilyScreenState()
    data class Error(val message: String) : FamilyScreenState()
}