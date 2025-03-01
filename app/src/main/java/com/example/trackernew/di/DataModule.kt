package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.data.db.AppDatabase
import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.repository.AddTaskRepositoryImpl
import com.example.trackernew.domain.repository.AddTaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindTasksRepository(tasksRepositoryImpl: AddTaskRepositoryImpl): AddTaskRepository

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