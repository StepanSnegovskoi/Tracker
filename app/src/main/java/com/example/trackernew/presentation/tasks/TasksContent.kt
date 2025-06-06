package com.example.trackernew.presentation.tasks

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.ui.theme.Green300
import com.example.trackernew.ui.theme.Orange100
import com.example.trackernew.ui.theme.Red300
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.Yellow100
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TIME_ADDING = 0.0f
private const val THIRD = 0.33f
private const val TWO_THIRDS = 0.66f
private const val ONE_FLOAT = 1f

@Composable
fun TasksContent(component: TasksComponent) {
    val state by component.model.collectAsState()

    val stateCategories = rememberSaveable {
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
        onDeleteIconCategoryClick = {
            component.onDeleteCategoryClicked(it)
        },
        onScheduleClick = {
            component.onScheduleClicked()
        },
        content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    ScaffoldTopAppBar(
                        state = state,
                        onSortItemClick = {
                            component.onSortChanged(it)
                        }
                    )
                },
                floatingActionButton = {
                    FAB(
                        onClick = {
                            component.onAddTaskClicked()
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = TrackerNewTheme.colors.linearGradientBackground)
                ) {
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
    when (val taskState = state.tasksState) {
        TasksStore.TasksState.Loading -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(
                    count = 5
                ) {
                    TaskItemLoading(Modifier.animateItem())
                }
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }

        is TasksStore.TasksState.Loaded -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(
                    items = taskState.tasks.filteredTasks,
                    key = { it.id }
                ) {
                    Task(
                        modifier = Modifier.animateItem(),
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
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Task(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskLongClick: () -> Unit,
    onDeleteIconClick: (Task) -> Unit
) {
    val stateDescription = rememberSaveable {
        mutableStateOf(value = false)
    }

    val tintColor = TrackerNewTheme.colors.tintColor

    val iconColorState = remember { mutableStateOf(tintColor) }

    val iconColor by animateColorAsState(
        targetValue = iconColorState.value,
        animationSpec = tween(durationMillis = 1000),
        label = "iconColorAnimation"
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {

            while (System.currentTimeMillis() < task.deadline) {
                iconColorState.value = calculateProgressColor(
                    task = task,
                    tintColor = tintColor
                )
                delay(1000)
            }
            iconColorState.value = Red300
        }
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
        colors = CardDefaults.cardColors(
            containerColor = TrackerNewTheme.colors.onBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                        when (task.status) {
                            TaskStatus.Completed -> R.drawable.question_24
                            TaskStatus.Executed -> R.drawable.done_24
                            TaskStatus.Failed -> R.drawable.not_completed_24
                            TaskStatus.InTheProcess -> R.drawable.dots_24
                        }
                    ),
                    contentDescription = null,
                    tint = iconColor
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(
                        R.drawable.hourglass
                    ),
                    contentDescription = null,
                    tint = iconColor
                )
            }
            AnimatedDescriptionAndDeadline(
                task = task,
                state = stateDescription,
                onDeleteIconClick = onDeleteIconClick
            )
        }
    }
}

private fun calculateProgressColor(
    task: Task,
    tintColor: Color
): Color {
    val totalTime = task.deadline - task.addingTime
    val progress = (System.currentTimeMillis() - task.addingTime).toFloat() / totalTime

    return when {
        progress < TIME_ADDING -> tintColor
        progress < THIRD -> Green300
        progress < TWO_THIRDS -> Yellow100
        progress < ONE_FLOAT -> Orange100
        else -> error("Incorrect progress $progress")
    }
}

@Composable
private fun TaskItemLoading(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingAnimation")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = TrackerNewTheme.colors.background,
        targetValue = TrackerNewTheme.colors.onBackground,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loadingColor"
    )

    Card(
        modifier = Modifier
            .then(other = modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = animatedColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = "",
                    color = Color.Transparent,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(R.drawable.question_24),
                    contentDescription = null,
                    tint = Color.Transparent
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(R.drawable.delete_outline_24),
                    tint = Color.Transparent,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ColumnScope.AnimatedDescriptionAndDeadline(
    modifier: Modifier = Modifier,
    task: Task,
    state: State<Boolean>,
    onDeleteIconClick: (Task) -> Unit
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Deadline(
                    modifier = Modifier
                        .padding(end = 4.dp, top = 8.dp)
                        .weight(1f),
                    task = task
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
            val color = if (it.isCompleted) Green300 else Red300
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.addingTime.toDateString(),
            fontSize = 12.sp,
            color = TrackerNewTheme.colors.textColor
        )
        if (task.deadline != 0L) {
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
}

@Composable
private fun CategoriesLazyColumn(
    modifier: Modifier = Modifier,
    state: TasksStore.State,
    onCategoryClick: (Category) -> Unit,
    onDeleteIconClick: (Category) -> Unit,
    onAddClick: () -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
    ) {
        items(
            items = state.categories,
            key = { it.name }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onCategoryClick(it)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = it.name,
                    fontSize = 18.sp,
                    color = TrackerNewTheme.colors.textColor
                )
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                onDeleteIconClick(it)
                            }
                        ),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = TrackerNewTheme.colors.tintColor
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onCategoryClick(Category(context.getString(R.string.all_together)))
                    },
                text = context.getString(R.string.all_together),
                fontSize = 18.sp,
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
                text = context.getString(R.string.add),
                fontSize = 18.sp,
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
    onDeleteIconClick: (Category) -> Unit,
    onAddClick: () -> Unit,
) {
    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = visibleState.value

    AnimatedVisibility(visibleState = transitionState) {
        CategoriesLazyColumn(
            modifier = modifier,
            state = state,
            onCategoryClick = onCategoryClick,
            onDeleteIconClick = {
                onDeleteIconClick(it)
            },
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
    onDeleteIconCategoryClick: (Category) -> Unit,
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
                drawerContainerColor = TrackerNewTheme.colors.onBackground,
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
                            text = stringResource(R.string.tasks),
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center,

                            )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onCategoriesClick()
                                },
                            text = stringResource(R.string.categories),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrackerNewTheme.colors.textColor
                        )
                        AnimatedCategoriesLazyColumn(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            state = state,
                            visibleState = stateCategories,
                            onCategoryClick = {
                                onCategoryClick(it)
                            },
                            onDeleteIconClick = {
                                onDeleteIconCategoryClick(it)
                            },
                            onAddClick = {
                                onAddCategoryClick()
                            }
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onScheduleClick()
                                },
                            text = stringResource(R.string.schedule),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrackerNewTheme.colors.textColor
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
fun FAB(
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

sealed class Sort(val value: String) {
    abstract fun comparator(): Comparator<Task>

    data object ByDateAdded : Sort(BY_DATE) {
        override fun comparator() = compareBy<Task> { it.addingTime }
    }

    data object ByDeadline : Sort(BY_DEADLINE) {
        override fun comparator() = compareBy<Task> { it.deadline }
    }

    data object ByName : Sort(BY_NAME) {
        override fun comparator() = compareBy<Task> { it.name }
    }

    private companion object {
        const val BY_DATE = "По дате добавления"
        const val BY_DEADLINE = "По дедлайну"
        const val BY_NAME = "По названию"
    }
}

val sortTypes = listOf(Sort.ByDateAdded, Sort.ByDeadline, Sort.ByName)