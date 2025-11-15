package com.example.duka.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,  // Assuming User entity exists
            parentColumns = ["id"],
            childColumns = ["assignedUserId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int,
    val name: String,
    val quantity: String?,
    val category: String?,
    val notes: String?,
    val assignedUserId: Int?,
    val isBought: Boolean = false,
    val boughtAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)