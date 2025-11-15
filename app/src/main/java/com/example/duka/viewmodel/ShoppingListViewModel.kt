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

    fun loadShoppingLists(familyId: Int) {
        viewModelScope.launch {
            _shoppingLists.value = shoppingListRepository.getShoppingListsByFamilyId(familyId)
        }
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.updateShoppingList(shoppingList)
        }
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.deleteShoppingList(shoppingList)
        }
    }
}
