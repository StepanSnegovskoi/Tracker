package com.example.trackernew

import android.app.Application
import com.example.trackernew.di.ApplicationScope
import com.example.trackernew.di.DaggerApplicationComponent

@ApplicationScope
class App : Application() {
    val component = DaggerApplicationComponent.factory().create(this)
}