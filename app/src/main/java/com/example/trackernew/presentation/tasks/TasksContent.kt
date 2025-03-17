package com.example.trackernew.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
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
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.utils.ADD
import com.example.trackernew.presentation.utils.INITIAL_CATEGORY_NAME
import com.example.trackernew.presentation.utils.Sort
import com.example.trackernew.presentation.utils.sortTypes
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.Red
import com.example.trackernew.ui.theme.TrackerNewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksContent(component: TasksComponent) {
    val state by component.model.collectAsState()
    val stateSortTypes = remember {
        mutableStateOf(false)
    }
    val stateCategories = remember {
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
        content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = TrackerNewTheme.colors.background,
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = TrackerNewTheme.colors.background
                        ),
                        title = {
                            Row {
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
                                        component.onSortChanged(it)
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
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            component.onAddTaskClicked()
                        },
                        containerColor = TrackerNewTheme.colors.onBackground,
                        contentColor = TrackerNewTheme.colors.oppositeColor
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                ) {
                    TasksLazyColumn(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp),
                        state = state,
                        component = component
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
    component: TasksComponent
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
    ) {
        items(
            items = state.tasks.filteredTasks,
            key = { it.id }
        ) {
            TaskItem(
                task = it,
                component = component,
                onLongClick = {
                    component.onTaskLongClicked(it)
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
    component: TasksComponent,
    task: Task,
    onLongClick: () -> Unit
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
                    onLongClick()
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
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier,
                    text = task.name,
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                if (task.isCompleted) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(horizontal = 4.dp),
                        painter = painterResource(R.drawable.done_24),
                        contentDescription = null,
                        tint = Green
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            component.onDeleteTaskClicked(task)
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
fun ColumnScope.AnimatedDescriptionAndDeadline(task: Task, state: State<Boolean>) {
    AnimatedVisibility(
        visible = state.value,
    ) {
        Column {
            Description(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                task = task
            )
            SubTasks(task)
            Deadline(task = task)
        }
    }
}

@Composable
fun SubTasks(task: Task) {
    val lineColor = TrackerNewTheme.colors.oppositeColor
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = lineColor, start = Offset(0f, size.height), end = Offset(
                        size.width,
                        size.height
                    )
                )
            }
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
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color
                )
            }

        }
        Spacer(
            modifier = Modifier
                .height(4.dp)
        )
    }

}

@Composable
fun Description(
    modifier: Modifier = Modifier,
    task: Task
) {
    if (task.description.isEmpty()) return
    val lineColor = TrackerNewTheme.colors.oppositeColor
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = lineColor, start = Offset(0f, size.height), end = Offset(
                        size.width,
                        size.height
                    )
                )
            }
            .then(modifier)
    ) {
        Text(
            text = task.description,
            color = TrackerNewTheme.colors.textColor
        )
    }
    Spacer(
        modifier = Modifier
            .height(4.dp)
    )
}

@Composable
fun Deadline(task: Task) {
    if (task.deadline == 0L) return
    Row(
        modifier = Modifier
            .padding(end = 4.dp, top = 8.dp)
            .fillMaxWidth(),
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
                fontSize = 16.sp,
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
                fontSize = 16.sp,
                text = ADD,
                color = TrackerNewTheme.colors.textColor
            )
        }
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSortTypes(
    expanded: State<Boolean>,
    onDismissRequest: () -> Unit,
    onItemClick: (Sort) -> Unit,
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
    state: TasksStore.State,
    stateCategories: State<Boolean>,
    onCategoriesClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onAddCategoryClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
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
                            text = "Todo List",
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
                        if (stateCategories.value) {
                            CategoriesLazyColumn(
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                state = state,
                                onCategoryClick = {
                                    onCategoryClick(it)
                                },
                                onAddClick = {
                                    onAddCategoryClick()
                                }
                            )
                        }
                    }
                }
            }
        },
        content = {
            content()
        }
    )
}