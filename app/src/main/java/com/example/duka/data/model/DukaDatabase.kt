package com.example.duka.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.duka.data.model.*
import com.example.duka.data.model.seedDatabase
import com.example.duka.data.dao.*
import com.example.duka.data.database.DukaDatabase.Companion.getDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.github.javafaker.Faker

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
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DukaDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}

private class DukaDatabaseCallback(
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val database = getDatabase(context) // Get the instance
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabase(database)
        }
    }
}
