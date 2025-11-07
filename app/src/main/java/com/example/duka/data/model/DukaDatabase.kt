package com.example.duka.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.duka.data.model.*
import com.example.duka.data.dao.*

@Database(
    entities = [User::class, Family::class, FamilyMember::class],
    version = 1,
    exportSchema = false
)
abstract class DukaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun familyDao(): FamilyDao
    abstract fun familyMemberDao(): FamilyMemberDao

    companion object {
        @Volatile
        private var INSTANCE: DukaDatabase? = null

        fun getDatabase(context: Context): DukaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DukaDatabase::class.java,
                    "duka_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
