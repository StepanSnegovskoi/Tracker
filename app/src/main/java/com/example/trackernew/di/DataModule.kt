package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.data.db.AppDatabase
import com.example.trackernew.data.db.dao.CategoryDao
import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.db.dao.TasksDao
import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.repository.AddAudienceRepositoryImpl
import com.example.trackernew.data.repository.AddCategoryRepositoryImpl
import com.example.trackernew.data.repository.AddLecturerRepositoryImpl
import com.example.trackernew.data.repository.AddLessonNameRepositoryImpl
import com.example.trackernew.data.repository.AddLessonRepositoryImpl
import com.example.trackernew.data.repository.AddTaskRepositoryImpl
import com.example.trackernew.data.repository.EditTaskRepositoryImpl
import com.example.trackernew.data.repository.TasksRepositoryImpl
import com.example.trackernew.data.repository.WeekRepositoryImpl
import com.example.trackernew.domain.repository.AddAudienceRepository
import com.example.trackernew.domain.repository.AddCategoryRepository
import com.example.trackernew.domain.repository.AddLecturerRepository
import com.example.trackernew.domain.repository.AddLessonNameRepository
import com.example.trackernew.domain.repository.AddLessonRepository
import com.example.trackernew.domain.repository.AddTaskRepository
import com.example.trackernew.domain.repository.EditTaskRepository
import com.example.trackernew.domain.repository.TasksRepository
import com.example.trackernew.domain.repository.WeekRepository
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

    @ApplicationScope
    @Binds
    fun bindAddLessonRepository(addLessonRepositoryImpl: AddLessonRepositoryImpl): AddLessonRepository

    @ApplicationScope
    @Binds
    fun bindAddLessonNameRepository(addLessonNameRepository: AddLessonNameRepositoryImpl): AddLessonNameRepository

    @ApplicationScope
    @Binds
    fun bindAddLecturerRepository(addLecturerRepository: AddLecturerRepositoryImpl): AddLecturerRepository

    @ApplicationScope
    @Binds
    fun bindAddAudienceRepository(addAudienceRepository: AddAudienceRepositoryImpl): AddAudienceRepository

    @ApplicationScope
    @Binds
    fun bindWeekRepository(weekRepositoryImpl: WeekRepositoryImpl): WeekRepository

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

        @ApplicationScope
        @Provides
        fun provideLessonDao(database: AppDatabase): LessonDao = database.lessonDao()

        @ApplicationScope
        @Provides
        fun provideWeekDao(database: AppDatabase): WeekDao = database.weekDao()
    }
}