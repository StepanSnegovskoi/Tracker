package com.example.trackernew.di

import android.content.Context
import com.example.trackernew.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component
    (
    modules = [
        DataModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}