package com.example.trackernew.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.trackernew.presentation.add.task.AddTaskComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class AddTask(val component: AddTaskComponent) : Child
    }
}