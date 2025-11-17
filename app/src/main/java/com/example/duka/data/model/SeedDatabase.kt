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
            username = if (i == 1) "testuser" else faker.name().username(), // Predictable username for user 1
            email = if (i == 1) "test@user.com" else faker.internet().emailAddress(),
            password = "password123",
            role = if (i == 1) "admin" else "user",
            avatarUrl = faker.internet().avatar()
        )
        val userId = database.userDao().insertUser(user).toInt()
        user.copy(id = userId)
    }

    // 2️⃣ Seed Families
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
            familyId = families[i].id,
            userId = users.random().id,
            role = if (i % 2 == 0) "owner" else "member"
        )
        database.familyMemberDao().insertMembership(member)
    }

    // 4️⃣ Seed ShoppingLists and ListItems
    families.forEach { family ->
        // Create 1 to 3 shopping lists per family
        repeat(faker.number().numberBetween(1, 4)) {
            val shoppingList = ShoppingList(
                familyId = family.id,
                name = "${faker.commerce().department()} Shopping",
                description = "Weekly groceries for the ${family.name} family",
                createdBy = users.random().id
            )
            val shoppingListId = database.shoppingListDao().insert(shoppingList).toInt()

            // Create 5 to 15 items per shopping list
            repeat(faker.number().numberBetween(5, 16)) {
                val listItem = ListItem(
                    listId = shoppingListId,
                    name = faker.commerce().productName(),
                    quantity = faker.number().numberBetween(1, 5).toString(),
                    category = faker.commerce().department(),
                    notes = if (faker.bool().bool()) faker.lorem().sentence() else null,
                    assignedUserId = if (faker.bool().bool()) users.random().id else null,
                    isBought = faker.bool().bool()
                )
                database.listItemDao().insert(listItem)
            }
        }
    }
}

// Optional helper to launch seeding from a coroutine
fun seedDatabaseAsync(database: DukaDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        seedDatabase(database)
    }
}
