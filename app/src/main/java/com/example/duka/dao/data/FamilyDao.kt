package com.example.duka.data.dao

import androidx.room.*
import com.example.duka.data.model.Family

@Dao
interface FamilyDao {

    // Insert a new family
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
