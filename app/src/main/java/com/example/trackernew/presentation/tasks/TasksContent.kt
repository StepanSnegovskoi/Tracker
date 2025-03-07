package com.example.trackernew.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
                topBar = {
                    TopAppBar(
                        title = {
                            Row {
                                Text(text = state.category.name)
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
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    TasksLazyColumn(
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
                        .size(32.dp)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            component.onDeleteTaskClicked(task)
                        },
                    painter = painterResource(R.drawable.delete_outline_24),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 4.dp),
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
            if (task.deadline != 0L) {
                Deadline(task = task)
            }
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
                text = it.name
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onCategoryClick(Category(INITIAL_CATEGORY_NAME))
                    },
                text = INITIAL_CATEGORY_NAME
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onAddClick()
                    },
                text = ADD
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
                modifier = Modifier
                    .width(340.dp)
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
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                onCategoriesClick()
                            },
                        text = "Категории",
                        fontSize = 18.sp
                    )
                    if (stateCategories.value){
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
        },
        content = {
            content()
        }
    )
}