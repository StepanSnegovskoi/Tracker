package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.data.db.AppDatabase
import com.example.trackernew.data.db.CategoryDao
import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.repository.AddCategoryRepositoryImpl
import com.example.trackernew.data.repository.AddTaskRepositoryImpl
import com.example.trackernew.data.repository.EditTaskRepositoryImpl
import com.example.trackernew.data.repository.TasksRepositoryImpl
import com.example.trackernew.domain.repository.AddCategoryRepository
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

    @ApplicationScope
    @Binds
    fun bindAddCategoryRepository(addCategoryRepositoryImpl: AddCategoryRepositoryImpl): AddCategoryRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideTasksDatabase(context: Context): AppDatabase =
            AppDatabase.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideTasksDao(database: AppDatabase): TasksDao = database.tasksDao()

        @ApplicationScope
        @Provides
        fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()
    }
}