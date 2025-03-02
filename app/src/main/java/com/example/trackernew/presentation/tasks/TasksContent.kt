package com.example.trackernew.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.utils.sortTypes
import java.util.Calendar
import kotlin.random.Random

private val tasks = buildList {
    repeat(times = 100) {
        add(
            Task(
                id = it,
                name = "Task $it",
                description = """Description Description Description
                                |Description Description Description
                                |Description Description Description
            """.trimMargin(),
                isCompleted = Random.nextBoolean(),
                addingTime = Calendar.getInstance().timeInMillis,
                category = "category",
                deadline = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_WEEK, 2)
                }.timeInMillis,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TasksContent() {
    val stateTopBar = remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(
                            modifier = Modifier,
                            fontSize = 20.sp,
                            text = "Общее"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Menu(
                            expanded = stateTopBar,
                            onDismissRequest = {
                                stateTopBar.value = false
                            },
                            onItemClick = {
                                stateTopBar.value = false
                            },
                            content = { modifier ->
                                Text(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clickable {
                                            stateTopBar.value = true
                                        }
                                        .then(modifier),
                                    fontSize = 16.sp,
                                    text = "По дате добавления"
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            TasksLazyColumn(tasks = tasks)
        }
    }
}

@Composable
private fun TasksLazyColumn(
    modifier: Modifier = Modifier,
    tasks: List<Task>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) {
            TaskItem(
                task = it
            )
        }
    }
}

@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task
) {
    val stateDescription = rememberSaveable {
        mutableStateOf(value = false)
    }
    Card(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                stateDescription.value = !stateDescription.value
            }
            .then(other = modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue.copy(alpha = 0.05f))
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier,
                    text = task.name
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    painter = painterResource(R.drawable.done_24),
                    contentDescription = null,
                    tint = if (task.isCompleted) Color(0xFF33D01E) else Color.Transparent,
                )
            }

            AnimatedDescriptionAndDeadline(
                task = task,
                state = stateDescription
            )

        }
    }
}

@Composable
fun ColumnScope.AnimatedDescriptionAndDeadline(task: Task, state: State<Boolean>) {
    AnimatedVisibility(
        visible = state.value,
    ) {
        Column {
            Description(task = task)
            Deadline(task = task)
        }
    }
}

@Composable
fun Description(task: Task) {
    Text(text = task.description)
}

@Composable
fun Deadline(task: Task) {
    Row(
        modifier = Modifier
            .padding(end = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.addingTime.toDateString(),
            fontSize = 12.sp
        )
        Text(
            text = "-",
            fontSize = 12.sp
        )
        Text(
            text = task.deadline.toDateString(),
            fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    expanded: State<Boolean>,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
        }
    ) {
        content(Modifier.menuAnchor())

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded.value,
            onDismissRequest = {
                onDismissRequest()
            }
        ) {
            sortTypes.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.value)
                    },
                    onClick = {
                        onItemClick(it.value)
                    }
                )
            }
        }
    }
}
