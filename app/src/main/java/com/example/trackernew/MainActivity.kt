package com.example.trackernew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.example.trackernew.presentation.root.DefaultRootComponent
import com.example.trackernew.presentation.root.RootContent
import com.example.trackernew.presentation.root.SnackbarManager
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponent: DefaultRootComponent.Factory

    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        val defaultComponentContext = defaultComponentContext()

        /**
         * Исправление постоянного белого фона клавиатуры в момент её появления
         */
        enableEdgeToEdge()

        setContent {
            RootContent(
                component = rootComponent.create(defaultComponentContext),
                snackbarManager = snackbarManager
            )
        }
    }
}
