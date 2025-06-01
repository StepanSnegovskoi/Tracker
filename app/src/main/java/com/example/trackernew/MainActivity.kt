package com.example.trackernew

import android.app.AlarmManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.example.trackernew.domain.repository.AlarmManagerRepository
import com.example.trackernew.presentation.root.DefaultRootComponent
import com.example.trackernew.presentation.root.RootContent
import com.example.trackernew.presentation.root.SnackbarManager
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponent: DefaultRootComponent.Factory

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var alarmManager: AlarmManagerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        val defaultComponentContext = defaultComponentContext()

        installSplashScreen()

        setContent {
            RootContent(
                component = rootComponent.create(defaultComponentContext),
                snackbarManager = snackbarManager,
                alarmManager = alarmManager
            )
        }
    }
}
