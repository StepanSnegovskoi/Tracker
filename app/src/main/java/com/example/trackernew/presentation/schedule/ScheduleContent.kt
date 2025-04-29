package com.example.trackernew.presentation.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.TypeOfLesson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleContent(component: ScheduleComponent) {
    val state by component.model.collectAsState()
    val weeks = state.weeks
    val days = weeks.flatMap { it.days }

    val pagerState = rememberPagerState(
        pageCount = { weeks.size * DAYS_IN_WEEK },
        initialPage = ZERO
    )
    val currentPage = remember {
        mutableIntStateOf(0)
    }

    ModalDrawer(
        onAddWeekClick = {
            component.onAddWeekButtonClick()
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (weeks.isNotEmpty()) {
                            Text(
                                text = weeks[currentPage.intValue / DAYS_IN_WEEK].name,
                            )
                        } else {
                            Text(
                                text = "Активные недели не выбраны",
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HorizontalPager(
                    state = pagerState,
                    key = { page -> page }
                ) { page ->
                    Column {
                        currentPage.intValue = page

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = days[page].name,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )

                        LazyColumn {
                            items(
                                items = days[page].lessons,
                                key = { it.id }
                            ) {
                                Lesson(it)
                            }
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .height(48.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                component.onAddWeekButtonClick()
                            },
                        text = "Добавить неделю",
                        fontSize = 18.sp
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                if (weeks.isNotEmpty()) {
                                    val lessons = days[pagerState.currentPage].lessons
                                    var futureLessonId = 0
                                    if (lessons.isNotEmpty()) {
                                        futureLessonId = lessons.maxOf { it.id } + 1
                                    }
                                    component.onAddLessonButtonClick(
                                        weeks[currentPage.intValue / DAYS_IN_WEEK].id.toString(),
                                        days[pagerState.currentPage].name,
                                        futureLessonId.toString()
                                    )
                                }
                            },
                        text = "Добавить занятие",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Lesson(lesson: Lesson) {
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                visible = !visible
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    text = lesson.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier,
                    text = lesson.start
                )
                Text(
                    modifier = Modifier,
                    text = lesson.end
                )
            }
            AnimatedVisibility(visible = visible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = lesson.lecturer)
                    Text(text = lesson.audience)

                    val typeOfLesson = when (lesson.typeOfLesson) {
                        TypeOfLesson.Another -> {
                            "Другое"
                        }

                        TypeOfLesson.Lesson -> {
                            "Лекция"
                        }

                        TypeOfLesson.Practise -> {
                            "практика"
                        }

                        else -> {
                            throw IllegalArgumentException("Unknown typeOfLesson ${lesson.typeOfLesson}")
                        }
                    }

                    Text(text = typeOfLesson)
                }
            }
        }
    }
}

@Composable
fun ModalDrawer(
    modifier: Modifier = Modifier,
    onAddWeekClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
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
                                .clickable {
                                    onAddWeekClick()
                                },
                            text = "Добавить неделю"
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

private const val DAYS_IN_WEEK = 7
private const val ZERO = 0