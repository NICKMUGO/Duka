package com.example.duka

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.duka.data.model.DukaDatabase
import com.example.duka.data.repository.FamilyRepository
import com.example.duka.ui.family.FamilyViewModel

object Injection {
    private fun provideFamilyRepository(context: Context): FamilyRepository {
        val database = DukaDatabase.getDatabase(context.applicationContext)
        return FamilyRepository(database.familyDao(), database.familyMemberDao())
    }

    fun provideFamilyViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(FamilyViewModel::class.java)) {
                    // For now, we assume a hardcoded user ID of 1. 
                    // This will be replaced by a real authentication system later.
                    val currentUserId = 1
                    return FamilyViewModel(provideFamilyRepository(context), currentUserId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}