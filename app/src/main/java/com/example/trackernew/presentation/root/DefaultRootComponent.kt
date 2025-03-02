package com.example.trackernew.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.trackernew.presentation.add.task.AddTaskStore
import com.example.trackernew.presentation.add.task.AddTaskStoreFactory
import com.example.trackernew.presentation.add.task.DefaultAddTaskComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.serializer

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val addTaskStoreFactory: DefaultAddTaskComponent.Factory
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        serializer = serializer(),
        initialConfiguration = Config.AddTask,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when(config){
            Config.AddTask -> {
                val component = addTaskStoreFactory.create(componentContext)
                RootComponent.Child.AddTask(component)
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }

    sealed interface Config {

        data object AddTask : Config
    }
}