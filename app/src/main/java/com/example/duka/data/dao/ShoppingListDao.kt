package com.example.duka.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.duka.data.model.ShoppingList

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(shoppingList: ShoppingList): Long

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getById(id: Int): ShoppingList?

    @Query("SELECT * FROM shopping_lists WHERE familyId = :familyId")
    suspend fun getByFamilyId(familyId: Int): List<ShoppingList>

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    suspend fun getAll(): List<ShoppingList>
}