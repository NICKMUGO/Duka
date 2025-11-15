package com.example.duka.data.repository

import com.example.duka.dao.data.ListItemDao
import com.example.duka.data.model.ListItem

class ListItemRepository(private val listItemDao: ListItemDao) {

    suspend fun insertListItem(listItem: ListItem) = listItemDao.insert(listItem)

    suspend fun updateListItem(listItem: ListItem) = listItemDao.update(listItem)

    suspend fun deleteListItem(listItem: ListItem) = listItemDao.delete(listItem)

    suspend fun getListItemById(id: Int) = listItemDao.getById(id)

    suspend fun getListItemsByListId(listId: Int) = listItemDao.getByListId(listId)

    suspend fun markItemAsBought(id: Int, isBought: Boolean) {
        val boughtAt = if (isBought) System.currentTimeMillis() else null
        listItemDao.markAsBought(id, isBought, boughtAt)
    }

    suspend fun getUnboughtItems() = listItemDao.getUnboughtItems()
}
