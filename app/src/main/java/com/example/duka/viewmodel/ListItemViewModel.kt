package com.example.duka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.model.ListItem
import com.example.duka.data.repository.ListItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListItemViewModel(private val listItemRepository: ListItemRepository) : ViewModel() {

    private val _listItems = MutableStateFlow<List<ListItem>>(emptyList())
    val listItems: StateFlow<List<ListItem>> = _listItems

    // Keep track of the current list to reload items automatically
    private var currentListId: Int? = null

    fun loadListItems(listId: Int) {
        currentListId = listId
        viewModelScope.launch {
            _listItems.value = listItemRepository.getListItemsByListId(listId)
        }
    }

    fun addListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.insertListItem(listItem)
            // Automatically refresh the list after adding
            currentListId?.let { loadListItems(it) }
        }
    }

    fun updateListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.updateListItem(listItem)
            // Automatically refresh the list after updating
            currentListId?.let { loadListItems(it) }
        }
    }

    fun deleteListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.deleteListItem(listItem)
            // Automatically refresh the list after deleting
            currentListId?.let { loadListItems(it) }
        }
    }

    fun markItemAsBought(id: Int, isBought: Boolean) {
        viewModelScope.launch {
            listItemRepository.markItemAsBought(id, isBought)
            // Automatically refresh the list after toggling the state
            currentListId?.let { loadListItems(it) }
        }
    }
}
