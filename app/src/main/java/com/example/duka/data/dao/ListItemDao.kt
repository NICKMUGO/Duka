package com.example.duka.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.duka.data.model.ListItem

@Dao
interface ListItemDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(listItem: ListItem): Long

    @Update
    suspend fun update(listItem: ListItem)

    @Delete
    suspend fun delete(listItem: ListItem)

    @Query("SELECT * FROM list_items WHERE id = :id")
    suspend fun getById(id: Int): ListItem?

    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY createdAt ASC")
    suspend fun getByListId(listId: Int): List<ListItem>

    @Query("UPDATE list_items SET isBought = :isBought, boughtAt = :boughtAt WHERE id = :id")
    suspend fun markAsBought(id: Int, isBought: Boolean, boughtAt: Long?)

    @Query("SELECT * FROM list_items WHERE isBought = 0 ORDER BY createdAt ASC")
    suspend fun getUnboughtItems(): List<ListItem>
}