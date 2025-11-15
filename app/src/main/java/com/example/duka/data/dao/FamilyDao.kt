package com.example.duka.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.duka.data.model.Family

@Dao
interface FamilyDao {

    // Insert a new family
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFamily(family: Family): Long

    // Retrieve all families
    @Query("SELECT * FROM families")
    suspend fun getAllFamilies(): List<Family>

    // Get a family by ID
    @Query("SELECT * FROM families WHERE id = :id")
    suspend fun getFamilyById(id: Int): Family?

    // Delete a family
    @Delete
    suspend fun deleteFamily(family: Family)
}