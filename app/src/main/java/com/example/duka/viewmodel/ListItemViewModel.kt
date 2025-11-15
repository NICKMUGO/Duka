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

    fun loadListItems(listId: Int) {
        viewModelScope.launch {
            _listItems.value = listItemRepository.getListItemsByListId(listId)
        }
    }

    fun addListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.insertListItem(listItem)
        }
    }

    fun updateListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.updateListItem(listItem)
        }
    }

    fun deleteListItem(listItem: ListItem) {
        viewModelScope.launch {
            listItemRepository.deleteListItem(listItem)
        }
    }

    fun markItemAsBought(id: Int, isBought: Boolean) {
        viewModelScope.launch {
            listItemRepository.markItemAsBought(id, isBought)
        }
    }
}
