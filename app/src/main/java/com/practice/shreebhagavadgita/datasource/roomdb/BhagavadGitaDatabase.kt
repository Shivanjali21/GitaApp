package com.practice.shreebhagavadgita.datasource.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedChapters::class, SavedVerses::class], version = 2, exportSchema = false)
@TypeConverters(BGitaTypeConverters::class)
abstract class BhagavadGitaDatabase : RoomDatabase() {

    abstract fun savedChaptersDao(): SavedChaptersDao
    abstract fun savedVersesDao(): SavedVersesDao

    companion object {
        //to get updated values on background threads we use this
        @Volatile
        var INSTANCE: BhagavadGitaDatabase? = null

        fun getDatabaseInstance(context: Context): BhagavadGitaDatabase? {
            val tempInstance = INSTANCE
            if (INSTANCE != null) return tempInstance
            synchronized(this) {
                val roomDb = Room.databaseBuilder(
                    context.applicationContext, BhagavadGitaDatabase::class.java,
                    "bhagavad_gita"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = roomDb
                return roomDb
            }
        }
    }
}
