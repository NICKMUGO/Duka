package com.example.duka.data.model

import androidx.room.*

@Entity(
    tableName = "family_members",
    foreignKeys = [
        ForeignKey(
            entity = Family::class,
            parentColumns = ["id"],
            childColumns = ["family_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["family_id"]), Index(value = ["user_id"])]
)
data class FamilyMember(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "family_id") val familyId: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    // The user's role in the family, e.g., "owner" or "member"
    val role: String = "member",
    val joinedAt: Long = System.currentTimeMillis()
)
