package com.example.tracker.presentation

import android.app.Application
import com.example.tracker.di.DaggerApplicationComponent

class App: Application(){

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}