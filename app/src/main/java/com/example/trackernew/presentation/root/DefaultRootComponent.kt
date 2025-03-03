package com.example.trackernew.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.add.category.DefaultAddCategoryComponent
import com.example.trackernew.presentation.add.task.AddTaskStore
import com.example.trackernew.presentation.add.task.AddTaskStoreFactory
import com.example.trackernew.presentation.add.task.DefaultAddTaskComponent
import com.example.trackernew.presentation.edit.DefaultEditTaskComponent
import com.example.trackernew.presentation.tasks.DefaultTasksComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val addTaskStoreFactory: DefaultAddTaskComponent.Factory,
    private val tasksStoreFactory: DefaultTasksComponent.Factory,
    private val editTaskStoreFactory: DefaultEditTaskComponent.Factory,
    private val addCategoryStoreFactory: DefaultAddCategoryComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        serializer = serializer(),
        initialConfiguration = Config.Tasks,
        handleBackButton = true,
        childFactory = ::child
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (val config = config) {
            Config.AddTask -> {
                val component = addTaskStoreFactory.create(
                    componentContext = componentContext,
                    ifCategoriesAreEmpty = {
                        navigation.push(Config.AddCategory)
                    },
                )
                RootComponent.Child.AddTask(component)
            }

            Config.Tasks -> {
                val component = tasksStoreFactory.create(
                    componentContext = componentContext,
                    onAddClick = {
                        navigation.push(Config.AddTask)
                    },
                    onTaskLongClick = {
                        navigation.push(Config.EditTask(it))
                    }
                )
                RootComponent.Child.Tasks(component)
            }

            is Config.EditTask -> {
                val component = editTaskStoreFactory.create(
                    componentContext = componentContext,
                    task = config.task
                )
                RootComponent.Child.EditTask(component)
            }

            Config.AddCategory -> {
                val component = addCategoryStoreFactory.create(
                    componentContext = componentContext
                )
                RootComponent.Child.AddCategory(component)
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object AddTask : Config

        @Serializable
        data class EditTask(val task: Task) : Config

        @Serializable
        data object Tasks : Config

        @Serializable
        data object AddCategory : Config
    }
}