package com.example.tracker.di

import android.app.Application
import com.example.tracker.presentation.activities.MainActivity
import com.example.tracker.presentation.fragments.FragmentAddCard
import com.example.tracker.presentation.fragments.FragmentAddGroup
import com.example.tracker.presentation.fragments.FragmentEditCard
import com.example.tracker.presentation.fragments.FragmentGroups
import com.example.tracker.presentation.fragments.FragmentHome
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModuleModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: FragmentAddGroup)

    fun inject(fragment: FragmentGroups)

    fun inject(fragment: FragmentHome)

    fun inject(fragment: FragmentAddCard)

    fun inject(fragment: FragmentEditCard)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}