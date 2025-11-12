package com.example.duka.data.dao

import androidx.room.*
import com.example.duka.data.model.FamilyMember

@Dao
interface FamilyMemberDao {

    // Add a new family member record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembership(member: FamilyMember): Long

    // Get all members in a family
    @Query("SELECT * FROM family_members WHERE family_id = :familyId")
    suspend fun getMembersByFamily(familyId: Int): List<FamilyMember>

    // Get all families a user belongs to
    @Query("SELECT * FROM family_members WHERE user_id = :userId")
    suspend fun getFamiliesByUser(userId: Int): List<FamilyMember>

    // Delete membership record
    @Delete
    suspend fun deleteMembership(member: FamilyMember)
}
