package com.example.trackernew.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.trackernew.presentation.add.category.AddCategoryComponent
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceComponent
import com.example.trackernew.presentation.add.lesson.lecturer.AddLecturerComponent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonComponent
import com.example.trackernew.presentation.add.lesson.name.AddLessonNameComponent
import com.example.trackernew.presentation.add.task.AddTaskComponent
import com.example.trackernew.presentation.add.week.AddWeekComponent
import com.example.trackernew.presentation.settings.ScheduleSettingsComponent
import com.example.trackernew.presentation.edit.task.EditTaskComponent
import com.example.trackernew.presentation.schedule.ScheduleComponent
import com.example.trackernew.presentation.tasks.TasksComponent
import com.example.trackernew.presentation.weeks.WeeksComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class AddTask(val component: AddTaskComponent) : Child

        data class Tasks(val component: TasksComponent) : Child

        data class EditTask(val component: EditTaskComponent) : Child

        data class AddCategory(val component: AddCategoryComponent) : Child

        data class AddLesson(val component: AddLessonComponent) : Child

        data class AddLessonName(val component: AddLessonNameComponent) : Child

        data class AddLecturer(val component: AddLecturerComponent) : Child

        data class AddAudience(val component: AddAudienceComponent) : Child

        data class Schedule(val component: ScheduleComponent) : Child

        data class AddWeek(val component: AddWeekComponent) : Child

        data class EditWeeks(val component: WeeksComponent) : Child

        data class ScheduleSettings(val component: ScheduleSettingsComponent) : Child
    }
}