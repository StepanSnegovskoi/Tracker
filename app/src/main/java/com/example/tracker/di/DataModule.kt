package com.example.tracker.di

import android.app.Application
import com.example.tracker.data.dao.CardDao
import com.example.tracker.data.dao.GroupDao
import com.example.tracker.data.database.AppDatabase
import com.example.tracker.data.repositoryimpl.RepositoryImpl
import com.example.tracker.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository


    companion object {

        @ApplicationScope
        @Provides
        fun provideCardDao(
            application: Application
        ): CardDao {
            return AppDatabase.getInstance(application).cardDao()
        }

        @ApplicationScope
        @Provides
        fun provideGroupDao(
            application: Application
        ): GroupDao {
            return AppDatabase.getInstance(application).groupDao()
        }
    }
}