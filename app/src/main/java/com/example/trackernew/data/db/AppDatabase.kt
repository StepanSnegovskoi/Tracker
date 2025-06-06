package com.example.trackernew.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackernew.data.db.converter.DaysConverter
import com.example.trackernew.data.db.converter.SubTasksConverter
import com.example.trackernew.data.db.converter.TaskStatusConverter
import com.example.trackernew.data.db.dao.CategoryDao
import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.db.dao.TasksDao
import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.CategoryDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.LessonNameDbModel
import com.example.trackernew.data.entity.TaskDbModel
import com.example.trackernew.data.entity.WeekDbModel

@Database(
    entities = [
        TaskDbModel::class,
        CategoryDbModel::class,
        AudienceDbModel::class,
        LecturerDbModel::class,
        LessonNameDbModel::class,
        WeekDbModel::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    SubTasksConverter::class,
    TaskStatusConverter::class,
    DaysConverter::class
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

    abstract fun lessonDao(): LessonDao

    abstract fun weekDao(): WeekDao
}