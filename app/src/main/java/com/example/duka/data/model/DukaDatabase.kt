package com.example.duka.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.duka.configs.Converters
import com.example.duka.data.dao.FamilyDao
import com.example.duka.data.dao.FamilyMemberDao
import com.example.duka.data.dao.ListItemDao
import com.example.duka.data.dao.ShoppingListDao
import com.example.duka.data.dao.UserDao


@Database(
    entities = [User::class, Family::class, FamilyMember::class, ShoppingList::class, ListItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DukaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun familyDao(): FamilyDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun listItemDao(): ListItemDao

    companion object {
        @Volatile
        private var INSTANCE: DukaDatabase? = null

        fun getDatabase(context: Context): DukaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DukaDatabase::class.java,
                    "duka_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}