package com.example.duka.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.duka.data.model.User

@Dao
interface UserDao {

    // Insert a new user into the database
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: User): Long

    // Retrieve all users
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    // Get a user by ID
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    // Delete a user
    @Delete
    suspend fun deleteUser(user: User)
}