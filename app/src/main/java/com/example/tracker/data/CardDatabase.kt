package com.example.tracker.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CardDbModel::class], version = 1, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {

    abstract fun dao(): CardDao

    companion object {

        private const val DB_NAME = "cards.db"
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
                ).build()
                    .apply {
                        instance = this
                        return this
                    }
            }
        }
    }
}
