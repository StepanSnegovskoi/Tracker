package com.example.tracker.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tracker.data.entities.CardDbModel
import com.example.tracker.data.entities.GroupDbModel
import com.example.tracker.data.dao.CardDao
import com.example.tracker.data.dao.GroupDao

@Database(entities = [CardDbModel::class, GroupDbModel::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun groupDao(): GroupDao

    companion object {

        private const val DB_NAME = "appDb.db"
        private var instance: AppDatabase? = null
        private val lock = Any()

        fun getInstance(application: Application): AppDatabase {
            instance?.let {
                return it
            }

            synchronized(lock) {
                instance?.let {
                    return it
                }

                Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                    .apply {
                        instance = this
                        return this
                    }
            }
        }
    }
}
