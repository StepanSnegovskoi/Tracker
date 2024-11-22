package com.example.tracker.di

import androidx.lifecycle.ViewModel
import com.example.tracker.presentation.viewModels.FragmentAddGroupViewModel
import com.example.tracker.presentation.viewModels.FragmentGroupsViewModel
import com.example.tracker.presentation.viewModels.FragmentHomeViewModel
import com.example.tracker.presentation.viewModels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModuleModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentAddGroupViewModel::class)
    fun bindFragmentAddGroupViewModel(viewModel: FragmentAddGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentGroupsViewModel::class)
    fun bindFragmentGroupsViewModel(viewModel: FragmentGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentHomeViewModel::class)
    fun bindFragmentHomeViewModel(viewModel: FragmentHomeViewModel): ViewModel
}