package com.example.duka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val role: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
