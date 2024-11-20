package com.example.tracker.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tracker.data.dbEntity.CardDbModel
import com.example.tracker.data.dbEntity.GroupDbModel
import com.example.tracker.data.dao.CardDao
import com.example.tracker.data.dao.GroupDao

@Database(entities = [CardDbModel::class, GroupDbModel::class], version = 2, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun groupDao(): GroupDao

    companion object {

        private const val DB_NAME = "appDb.db"
        private var instance: CardDatabase? = null
        private val lock = Any()

        fun getInstance(application: Application): CardDatabase {
            instance?.let {
                return it
            }

            synchronized(lock) {
                instance?.let {
                    return it
                }

                Room.databaseBuilder(
                    application,
                    CardDatabase::class.java,
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
