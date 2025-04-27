package com.example.trackernew.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.trackernew.presentation.add.category.AddCategoryComponent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonComponent
import com.example.trackernew.presentation.add.task.AddTaskComponent
import com.example.trackernew.presentation.edit.EditTaskComponent
import com.example.trackernew.presentation.tasks.TasksComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class AddTask(val component: AddTaskComponent) : Child

        data class Tasks(val component: TasksComponent) : Child

        data class EditTask(val component: EditTaskComponent) : Child

        data class AddCategory(val component: AddCategoryComponent) : Child

        data class AddLesson(val component: AddLessonComponent) : Child
    }
}