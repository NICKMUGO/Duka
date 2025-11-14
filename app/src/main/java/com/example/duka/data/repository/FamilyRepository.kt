package com.example.duka.data.repository

import com.example.duka.dao.data.FamilyDao
import com.example.duka.dao.data.FamilyMemberDao
import com.example.duka.data.model.Family
import com.example.duka.data.model.FamilyMember

/**
 * A data class that combines Family data with the user-specific role.
 * This is what the UI will consume.
 */
data class FamilyDetails(
    val family: Family,
    val isOwner: Boolean
)

class FamilyRepository(
    private val familyDao: FamilyDao,
    private val familyMemberDao: FamilyMemberDao
) {

    /**
     * Retrieves all families for a given user and transforms them into FamilyDetails,
     * which includes the isOwner flag needed by the UI.
     */
    suspend fun getFamilyDetailsForUser(userId: Int): List<FamilyDetails> {
        // Get all the memberships for the user
        val memberships = familyMemberDao.getFamiliesByUser(userId)

        // For each membership, get the full family details and determine ownership
        return memberships.mapNotNull { membership ->
            familyDao.getFamilyById(membership.familyId)?.let {
                FamilyDetails(
                    family = it,
                    isOwner = membership.role == "owner"
                )
            }
        }
    }

    /**
     * Creates a new family and automatically makes the creator the owner.
     */
    suspend fun createFamily(familyName: String, ownerUserId: Int) {
        // Create the new family
        val newFamily = Family(name = familyName)
        val familyId = familyDao.insertFamily(newFamily).toInt()

        // Create the membership record that makes the user the owner
        val membership = FamilyMember(
            familyId = familyId,
            userId = ownerUserId,
            role = "owner"
        )
        familyMemberDao.insertMembership(membership)
    }

    /**
     * Deletes an entire family. This should only be done by an owner.
     */
    suspend fun deleteFamily(familyId: Int) {
        // The database will cascade the delete to family_members automatically
        familyDao.getFamilyById(familyId)?.let {
            familyDao.deleteFamily(it)
        }
    }

    /**
     * Allows a user to leave a family.
     */
    suspend fun leaveFamily(familyId: Int, userId: Int) {
        val membership = familyMemberDao.getMembersByFamily(familyId)
            .find { it.userId == userId }

        if (membership != null) {
            familyMemberDao.deleteMembership(membership)
        }
    }

    /**
     * Updates the name of a family.
     */
    suspend fun updateFamilyName(familyId: Int, newName: String) {
        familyDao.getFamilyById(familyId)?.let {
            familyDao.insertFamily(it.copy(name = newName))
        }
    }
}