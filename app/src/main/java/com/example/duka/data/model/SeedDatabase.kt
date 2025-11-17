package com.example.duka.data.model

import com.example.duka.data.database.DukaDatabase
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend fun seedDatabase(database: DukaDatabase) {
    val faker = Faker()

    val users = (1..10).map {
        val user = User(
            username = faker.name().username(),
            email = faker.internet().emailAddress(),
            password = "password123",
            role = if (it % 2 == 0) "admin" else "user",
            avatarUrl = faker.internet().avatar()
        )
        val userId = database.userDao().insertUser(user).toInt() // get generated ID
        user.copy(id = userId)
    }

    val families = (1..10).map {
        val family = Family(
            name = faker.name().lastName(),
            memberCount = 1
        )
        val familyId = database.familyDao().insertFamily(family).toInt() // get generated ID
        family.copy(id = familyId)
    }

    // 3️⃣ Seed FamilyMembers
    (1..10).forEach { i ->
        val member = FamilyMember(
            familyId = families.random().id, // valid family ID
            userId = users.random().id,      // valid user ID
            role = if (i % 3 == 0) "owner" else "member"
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
