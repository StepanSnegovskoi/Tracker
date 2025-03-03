package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.data.db.AppDatabase
import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.repository.AddTaskRepositoryImpl
import com.example.trackernew.data.repository.EditTaskRepositoryImpl
import com.example.trackernew.data.repository.TasksRepositoryImpl
import com.example.trackernew.domain.repository.AddTaskRepository
import com.example.trackernew.domain.repository.EditTaskRepository
import com.example.trackernew.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindAddTaskRepository(tasksRepositoryImpl: AddTaskRepositoryImpl): AddTaskRepository

    @ApplicationScope
    @Binds
    fun bindTasksRepository(tasksRepositoryImpl: TasksRepositoryImpl): TasksRepository

    @ApplicationScope
    @Binds
    fun bindEditTaskRepository(editTaskRepositoryImpl: EditTaskRepositoryImpl): EditTaskRepository

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