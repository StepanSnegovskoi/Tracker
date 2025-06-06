package com.example.trackernew.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.add.category.DefaultAddCategoryComponent
import com.example.trackernew.presentation.add.lesson.audience.DefaultAddAudienceComponent
import com.example.trackernew.presentation.add.lesson.lecturer.DefaultAddLecturerComponent
import com.example.trackernew.presentation.add.lesson.lesson.DefaultAddLessonComponent
import com.example.trackernew.presentation.add.lesson.name.DefaultAddLessonNameComponent
import com.example.trackernew.presentation.add.task.DefaultAddTaskComponent
import com.example.trackernew.presentation.add.week.DefaultAddWeekComponent
import com.example.trackernew.presentation.edit.task.DefaultEditTaskComponent
import com.example.trackernew.presentation.schedule.DefaultScheduleComponent
import com.example.trackernew.presentation.settings.DefaultScheduleSettingsComponent
import com.example.trackernew.presentation.tasks.DefaultTasksComponent
import com.example.trackernew.presentation.weeks.DefaultWeeksComponent
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
    private val addCategoryStoreFactory: DefaultAddCategoryComponent.Factory,
    private val addLessonStoreFactory: DefaultAddLessonComponent.Factory,
    private val addLessonNameStoreFactory: DefaultAddLessonNameComponent.Factory,
    private val addAudienceStoreFactory: DefaultAddAudienceComponent.Factory,
    private val scheduleStoreFactory: DefaultScheduleComponent.Factory,
    private val addWeekStoreFactory: DefaultAddWeekComponent.Factory,
    private val weeksStoreFactory: DefaultWeeksComponent.Factory,
    private val scheduleSettingsFactory: DefaultScheduleSettingsComponent.Factory,
    private val addLecturerStoreFactory: DefaultAddLecturerComponent.Factory
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
        return when (config) {
            Config.AddTask -> {
                val component = addTaskStoreFactory.create(
                    componentContext = componentContext,
                    onCategoriesListIsEmpty = {
                        navigation.push(Config.AddCategory)
                    },
                    onTaskSaved = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.AddTask(component)
            }

            Config.Tasks -> {
                val component = tasksStoreFactory.create(
                    componentContext = componentContext,
                    onAddTaskClicked = {
                        navigation.push(Config.AddTask)
                    },
                    onTaskLongClicked = {
                        navigation.push(Config.EditTask(it))
                    },
                    onAddCategoryClicked = {
                        navigation.push(Config.AddCategory)
                    },
                    onScheduleClicked = {
                        navigation.push(Config.Schedule)
                    }
                )
                RootComponent.Child.Tasks(component)
            }

            is Config.EditTask -> {
                val component = editTaskStoreFactory.create(
                    componentContext = componentContext,
                    task = config.task,
                    onTaskEdited = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.EditTask(component)
            }

            Config.AddCategory -> {
                val component = addCategoryStoreFactory.create(
                    componentContext = componentContext,
                    onCategorySaved = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.AddCategory(component)
            }

            is Config.AddLesson -> {
                val component = addLessonStoreFactory.create(
                    componentContext = componentContext,
                    weekId = config.weekId,
                    dayName = config.dayName,
                    futureLessonId = config.futureLessonId,
                    onLessonNamesListIsEmpty = {
                        navigation.push(Config.AddLessonName)
                    },
                    onLecturersListIsEmpty = {
                        navigation.push(Config.AddLecturer)
                    },
                    onAudiencesListIsEmpty = {
                        navigation.push(Config.AddAudience)
                    },
                    onLessonSaved = {
                        navigation.pop()
                    },
                )

                RootComponent.Child.AddLesson(component)
            }

            Config.AddLessonName -> {
                val component = addLessonNameStoreFactory.create(
                    componentContext = componentContext,
                    onLessonNameSaved = {
                        navigation.pop()
                    }
                )

                RootComponent.Child.AddLessonName(component)
            }

            Config.AddLecturer -> {
                val component = addLecturerStoreFactory.create(
                    componentContext = componentContext,
                    onLecturerSaved = {
                        navigation.pop()
                    }
                )

                RootComponent.Child.AddLecturer(component)
            }

            Config.AddAudience -> {
                val component = addAudienceStoreFactory.create(
                    componentContext = componentContext,
                    onAudienceSaved = {
                        navigation.pop()
                    }
                )

                RootComponent.Child.AddAudience(component)
            }

            Config.Schedule -> {
                val component = scheduleStoreFactory.create(
                    componentContext = componentContext,
                    onAddWeekClicked = {
                        navigation.push(Config.AddWeek)
                    },
                    onAddLessonClicked = { weekId, dayName, futureLessonId ->
                        navigation.push(Config.AddLesson(weekId, dayName, futureLessonId))
                    },
                    onEditWeeksClicked = {
                        navigation.push(Config.EditWeeks)
                    },
                    onSettingsClicked = {
                        navigation.push(Config.ScheduleSettings)
                    }
                )

                RootComponent.Child.Schedule(component)
            }

            Config.AddWeek -> {
                val component = addWeekStoreFactory.create(
                    componentContext = componentContext,
                    onWeekSaved = {
                        navigation.pop()
                    }
                )

                RootComponent.Child.AddWeek(component)
            }

            Config.EditWeeks -> {
                val component = weeksStoreFactory.create(
                    componentContext = componentContext,
                    onConfirmEditButtonClicked = {
                        navigation.pop()
                    }
                )

                RootComponent.Child.EditWeeks(component)
            }

            Config.ScheduleSettings -> {
                val component = scheduleSettingsFactory.create(
                    componentContext = componentContext
                )

                RootComponent.Child.ScheduleSettings(component)
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

        @Serializable
        data class AddLesson(
            val weekId: Int,
            val dayName: String,
            val futureLessonId: Int
        ) : Config

        @Serializable
        data object AddLessonName : Config

        @Serializable
        data object AddLecturer : Config

        @Serializable
        data object AddAudience : Config

        @Serializable
        data object Schedule : Config

        @Serializable
        data object AddWeek : Config

        @Serializable
        data object EditWeeks : Config

        @Serializable
        data object ScheduleSettings : Config
    }
}