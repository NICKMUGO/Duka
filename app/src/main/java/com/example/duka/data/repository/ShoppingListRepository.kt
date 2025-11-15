package com.example.duka.data.repository

import com.example.duka.data.dao.ShoppingListDao
import com.example.duka.data.model.ShoppingList

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {

    suspend fun insertShoppingList(shoppingList: ShoppingList) = shoppingListDao.insert(shoppingList)

    suspend fun updateShoppingList(shoppingList: ShoppingList) = shoppingListDao.update(shoppingList)

    suspend fun deleteShoppingList(shoppingList: ShoppingList) = shoppingListDao.delete(shoppingList)

    suspend fun getShoppingListById(id: Int) = shoppingListDao.getById(id)

    suspend fun getShoppingListsByFamilyId(familyId: Int) = shoppingListDao.getByFamilyId(familyId)

    suspend fun getAllShoppingLists() = shoppingListDao.getAll()
}
