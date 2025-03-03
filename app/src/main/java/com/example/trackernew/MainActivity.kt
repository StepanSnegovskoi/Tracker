package com.example.trackernew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.trackernew.presentation.root.DefaultRootComponent
import com.example.trackernew.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponent: DefaultRootComponent.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)

        val defaultComponentContext = defaultComponentContext()

        setContent {
            RootContent(component = rootComponent.create(defaultComponentContext))
        }
    }
}
