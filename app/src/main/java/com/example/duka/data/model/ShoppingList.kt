package com.example.duka.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_lists",
    foreignKeys = [
        ForeignKey(
            entity = Family::class,  // Assuming Family entity exists
            parentColumns = ["id"],
            childColumns = ["familyId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,  // Assuming User entity exists
            parentColumns = ["id"],
            childColumns = ["createdBy"]
        )
    ]
)
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val familyId: Int,
    val name: String,
    val description: String?,
    val createdBy: Int?,
    val createdAt: Long = System.currentTimeMillis(),  // Use Long for timestamps in Room
    val updatedAt: Long = System.currentTimeMillis()
)