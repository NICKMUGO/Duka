package com.example.duka.data.model

import com.example.duka.data.database.DukaDatabase
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend fun seedDatabase(database: DukaDatabase) {
    val faker = Faker()

    // 1️⃣ Seed Users
    val users = (1..10).map { i ->
        val user = User(
            username = faker.name().username(),
            email = faker.internet().emailAddress(),
            password = "password123",
            role = if (i == 1) "admin" else "user",
            avatarUrl = faker.internet().avatar()
        )
        val userId = database.userDao().insertUser(user).toInt() // get generated ID
        user.copy(id = userId)
    }

    val families = (1..10).map {
        val family = Family(
            name = "${faker.gameOfThrones().house()} Family",
            memberCount = 1
        )
        val familyId = database.familyDao().insertFamily(family).toInt()
        family.copy(id = familyId)
    }

    // 3️⃣ Seed FamilyMembers with predictable owners
    // Ensure our main user (ID=1) is an owner of the first 3 families
    val mainTestUser = users.first()
    (0..2).forEach {
        val member = FamilyMember(
            familyId = families[it].id,
            userId = mainTestUser.id,
            role = "owner"
        )
        database.familyMemberDao().insertMembership(member)
    }

    // Add some other random members to other families
    (3..9).forEach { i ->
        val member = FamilyMember(
            familyId = families.random().id, // valid family ID
            userId = users.random().id,      // valid user ID
            role = if (i % 3 == 0) "owner" else "member",
            isOwner = i % 3 == 0
        )
        database.familyMemberDao().insertMembership(member)
    }
}

// Optional helper to launch seeding from a coroutine
fun seedDatabaseAsync(database: DukaDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        seedDatabase(database)
    }
}
