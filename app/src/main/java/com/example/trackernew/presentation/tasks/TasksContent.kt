package com.example.trackernew.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.tasks.TasksStore.Label
import com.example.trackernew.presentation.utils.ADD
import com.example.trackernew.presentation.utils.INITIAL_CATEGORY_NAME
import com.example.trackernew.presentation.utils.Sort
import com.example.trackernew.presentation.utils.sortTypes
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.Orange
import com.example.trackernew.ui.theme.Red
import com.example.trackernew.ui.theme.TrackerNewTheme

@Composable
fun TasksContent(component: TasksComponent) {
    val state by component.model.collectAsState()
    val stateCategories = rememberSaveable() {
        mutableStateOf(false)
    }

    ModalDrawer(
        state = state,
        stateCategories = stateCategories,
        onCategoriesClick = {
            stateCategories.value = !stateCategories.value
        },
        onCategoryClick = {
            component.onCategoryChanged(it)
        },
        onAddCategoryClick = {
            component.onAddCategoryClicked()
        },
        onScheduleClick = {
            component.onScheduleClicked()
        },
        content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = TrackerNewTheme.colors.background,
                topBar = {
                    ScaffoldTopAppBar(
                        state = state,
                        onSortItemClick = {
                            component.onSortChanged(it)
                        }
                    )
                },
                floatingActionButton = {
                    ScaffoldFloatingActionButton(
                        onClick = {
                            component.onAddTaskClicked()
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                ) {
                    TasksLazyColumn(
                        modifier = Modifier
                            .padding(4.dp),
                        state = state,
                        onTaskLongClick = {
                            component.onTaskLongClicked(it)
                        },
                        onDeleteIconClick = {
                            component.onDeleteTaskClicked(it)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun TasksLazyColumn(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    onTaskLongClick: (Task) -> Unit,
    onDeleteIconClick: (Task) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(
            items = state.tasks.filteredTasks,
            key = { it.id }
        ) {
            TaskItem(
                task = it,
                onTaskLongClick = {
                    onTaskLongClick(it)
                },
                onDeleteIconClick = {
                    onDeleteIconClick(it)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskLongClick: () -> Unit,
    onDeleteIconClick: (Task) -> Unit
) {
    val stateDescription = rememberSaveable {
        mutableStateOf(value = false)
    }

    Card(
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    stateDescription.value = !stateDescription.value
                },
                onLongClick = {
                    onTaskLongClick()
                }
            )
            .then(other = modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = TrackerNewTheme.colors.onBackground)
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = task.name,
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(
                        when(task.status){
                            TaskStatus.Completed -> R.drawable.question_24
                            TaskStatus.Executed -> R.drawable.done_24
                            TaskStatus.Failed -> R.drawable.not_completed_24
                            TaskStatus.InTheProcess -> R.drawable.dots_24
                        }
                    ),
                    contentDescription = null,
                    tint = when(task.status){
                        TaskStatus.Completed -> TrackerNewTheme.colors.oppositeColor
                        TaskStatus.Executed -> Green
                        TaskStatus.Failed -> Red
                        TaskStatus.InTheProcess -> Orange
                    }
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            onDeleteIconClick(task)
                        },
                    painter = painterResource(R.drawable.delete_outline_24),
                    tint = TrackerNewTheme.colors.tintColor,
                    contentDescription = null
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
fun ColumnScope.AnimatedDescriptionAndDeadline(
    modifier: Modifier = Modifier,
    task: Task,
    state: State<Boolean>
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = state.value,
    ) {
        Column {
            Description(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                task = task
            )
            SubTasks(task = task)
            Deadline(
                modifier = Modifier
                    .padding(end = 4.dp, top = 8.dp)
                    .fillMaxWidth(),
                task = task
            )
            val lineColor = TrackerNewTheme.colors.oppositeColor
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .drawBehind {
                        drawLine(
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            color = lineColor
                        )
                    }
            )
        }
    }
}

@Composable
fun SubTasks(
    modifier: Modifier = Modifier,
    task: Task
) {
    if (task.subTasks.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {

        task.subTasks.forEach {
            val icon = if (it.isCompleted) R.drawable.done_24 else R.drawable.not_completed_24
            val color = if (it.isCompleted) Green else Red
            Row {
                Text(
                    text = it.name,
                    color = TrackerNewTheme.colors.textColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color
                )
            }
        }
    }
}

@Composable
fun Description(
    modifier: Modifier = Modifier,
    task: Task
) {
    if (task.description.isEmpty()) return
    Column(
        modifier = modifier
    ) {
        Text(
            text = task.description,
            color = TrackerNewTheme.colors.textColor
        )
    }
}

@Composable
fun Deadline(
    modifier: Modifier = Modifier,
    task: Task
) {
    if (task.deadline == 0L) return
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.addingTime.toDateString(),
            fontSize = 12.sp,
            color = TrackerNewTheme.colors.textColor
        )
        Text(
            text = "-",
            fontSize = 12.sp,
            color = TrackerNewTheme.colors.textColor
        )
        Text(
            text = task.deadline.toDateString(),
            fontSize = 12.sp,
            color = TrackerNewTheme.colors.textColor
        )
    }
}

@Composable
private fun CategoriesLazyColumn(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    onCategoryClick: (Category) -> Unit,
    onAddClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
    ) {
        items(
            items = state.categories,
            key = { it.name }
        ) {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onCategoryClick(it)
                    },
                fontSize = 16.sp,
                text = it.name,
                color = TrackerNewTheme.colors.textColor
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onCategoryClick(Category(INITIAL_CATEGORY_NAME))
                    },
                text = INITIAL_CATEGORY_NAME,
                color = TrackerNewTheme.colors.textColor
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onAddClick()
                    },
                text = ADD,
                color = TrackerNewTheme.colors.textColor
            )
        }
    }
}

@Composable
private fun ColumnScope.AnimatedCategoriesLazyColumn(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    visibleState: State<Boolean>,
    onCategoryClick: (Category) -> Unit,
    onAddClick: () -> Unit,
) {
    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = visibleState.value

    AnimatedVisibility(visibleState = transitionState) {
        CategoriesLazyColumn(
            modifier = modifier,
            state = state,
            onCategoryClick = onCategoryClick,
            onAddClick = onAddClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSortTypes(
    modifier: Modifier = Modifier,
    expanded: State<Boolean>,
    onDismissRequest: () -> Unit,
    onItemClick: (Sort) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded.value,
        onExpandedChange = {
        }
    ) {
        content(Modifier.menuAnchor())

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(TrackerNewTheme.colors.onBackground),
            expanded = expanded.value,
            onDismissRequest = {
                onDismissRequest()
            }
        ) {
            sortTypes.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it.value,
                            color = TrackerNewTheme.colors.textColor
                        )
                    },
                    onClick = {
                        onItemClick(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ModalDrawer(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    stateCategories: State<Boolean>,
    onCategoriesClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onAddCategoryClick: () -> Unit,
    onScheduleClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = TrackerNewTheme.colors.background,
                modifier = Modifier
                    .width(340.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            text = "Задачи",
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center,

                            )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    onCategoriesClick()
                                },
                            text = "Категории",
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 20.sp
                        )
                        AnimatedCategoriesLazyColumn(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            state = state,
                            visibleState = stateCategories,
                            onCategoryClick = {
                                onCategoryClick(it)
                            },
                            onAddClick = {
                                onAddCategoryClick()
                            }
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    onScheduleClick()
                                },
                            text = "Расписание",
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        },
        content = {
            content()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopAppBar(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    onSortItemClick: (Sort) -> Unit
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TrackerNewTheme.colors.background
        ),
        title = {
            TopAppBarTitle(
                state = state,
                onSortItemClick = {
                    onSortItemClick(it)
                }
            )
        }
    )
}

@Composable
fun TopAppBarTitle(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    onSortItemClick: (Sort) -> Unit
) {
    val stateSortTypes = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
    ) {
        Text(
            text = state.category.name,
            color = TrackerNewTheme.colors.textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        MenuSortTypes(
            expanded = stateSortTypes,
            onDismissRequest = {
                stateSortTypes.value = false
            },
            onItemClick = {
                onSortItemClick(it)
                stateSortTypes.value = false
            },
            content = { modifier ->
                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable {
                            stateSortTypes.value = true
                        }
                        .then(modifier),
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 16.sp,
                    text = state.sort.value
                )
            }
        )
    }
}

@Composable
fun ScaffoldFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    FloatingActionButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        containerColor = TrackerNewTheme.colors.onBackground,
        contentColor = TrackerNewTheme.colors.oppositeColor
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}