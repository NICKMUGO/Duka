package com.example.duka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.model.ShoppingList
import com.example.duka.data.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val shoppingListRepository: ShoppingListRepository) : ViewModel() {

    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    // Keep track of the current family to reload lists automatically
    private var currentFamilyId: Int? = null

    fun loadShoppingLists(familyId: Int) {
        currentFamilyId = familyId
        viewModelScope.launch {
            _shoppingLists.value = shoppingListRepository.getShoppingListsByFamilyId(familyId)
        }
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
            // Automatically refresh the list after adding
            currentFamilyId?.let { loadShoppingLists(it) }
        }
    }

    fun updateShoppingList(listId: Int, newName: String, newDescription: String?) {
        viewModelScope.launch {
            val originalList = shoppingListRepository.getShoppingListById(listId)
            if (originalList != null) {
                val updatedList = originalList.copy(
                    name = newName,
                    description = newDescription,
                    updatedAt = System.currentTimeMillis()
                )
                shoppingListRepository.updateShoppingList(updatedList)
                // Automatically refresh the list after updating
                currentFamilyId?.let { loadShoppingLists(it) }
            }
        }
    }

    fun deleteShoppingList(listId: Int) {
        viewModelScope.launch {
            val listToDelete = shoppingListRepository.getShoppingListById(listId)
            if (listToDelete != null) {
                shoppingListRepository.deleteShoppingList(listToDelete)
                // Automatically refresh the list after deleting
                currentFamilyId?.let { loadShoppingLists(it) }
            }
        }
    }
}
