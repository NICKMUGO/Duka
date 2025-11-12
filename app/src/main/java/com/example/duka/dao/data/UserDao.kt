package com.example.duka.dao.data

import androidx.room.*
import com.example.duka.data.model.User

@Dao
interface UserDao {

    // Insert a new user into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
