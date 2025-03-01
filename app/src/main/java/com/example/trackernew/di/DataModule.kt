package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.data.db.AppDatabase
import com.example.trackernew.data.db.TasksDao
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideTasksDatabase(context: Context): AppDatabase =
            AppDatabase.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideTasksDao(database: AppDatabase): TasksDao = database.dao()
    }

}