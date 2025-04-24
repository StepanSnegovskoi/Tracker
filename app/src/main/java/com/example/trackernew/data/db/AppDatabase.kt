package com.example.trackernew.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackernew.data.entity.CategoryDbModel
import com.example.trackernew.data.entity.TaskDbModel

@Database(
    entities = [
        TaskDbModel::class,
        CategoryDbModel::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    SubTasksConverter::class,
    TaskStatusConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "db"
        private val LOCK = Unit

        fun getInstance(appContext: Context): AppDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                Room.databaseBuilder(
                    context = appContext,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                ).fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                        return it
                    }
            }
        }
    }

    abstract fun tasksDao(): TasksDao

    abstract fun categoryDao(): CategoryDao
}