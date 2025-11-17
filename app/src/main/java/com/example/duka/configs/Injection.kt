package com.example.duka.configs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.duka.data.database.DukaDatabase
import com.example.duka.data.repository.FamilyRepository
import com.example.duka.data.repository.ListItemRepository
import com.example.duka.data.repository.ShoppingListRepository
import com.example.duka.ui.family.FamilyViewModel
import com.example.duka.viewmodel.ListItemViewModel
import com.example.duka.viewmodel.ShoppingListViewModel

object Injection {
    private fun provideFamilyRepository(context: Context): FamilyRepository {
        val database = DukaDatabase.getDatabase(context.applicationContext)
        return FamilyRepository(database.familyDao(), database.familyMemberDao())
    }

    private fun provideShoppingListRepository(context: Context): ShoppingListRepository {
        val database = DukaDatabase.getDatabase(context.applicationContext)
        return ShoppingListRepository(database.shoppingListDao())
    }

    private fun provideListItemRepository(context: Context): ListItemRepository {
        val database = DukaDatabase.getDatabase(context.applicationContext)
        return ListItemRepository(database.listItemDao())
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(FamilyViewModel::class.java) -> {
                        val currentUserId = 1 // Hardcoded user ID
                        FamilyViewModel(provideFamilyRepository(context), currentUserId) as T
                    }
                    modelClass.isAssignableFrom(ShoppingListViewModel::class.java) -> {
                        ShoppingListViewModel(provideShoppingListRepository(context)) as T
                    }
                    modelClass.isAssignableFrom(ListItemViewModel::class.java) -> {
                        ListItemViewModel(provideListItemRepository(context)) as T
                    }
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
