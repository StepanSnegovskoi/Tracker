package com.example.trackernew.presentation.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.presentation.extensions.toTimeString
import com.example.trackernew.ui.theme.TrackerNewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleContent(component: ScheduleComponent) {
    val state by component.model.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = { state.weeks.size * DAYS_IN_WEEK },
        initialPage = ZERO
    )

    val days = state.weeks.flatMap { it.days }

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (state.weeks.isNotEmpty()) {
                            true -> {
                                Text(
                                    modifier = Modifier
                                        .weight(1f),
                                    text = state.weeks[pagerState.currentPage / DAYS_IN_WEEK].name,
                                    color = TrackerNewTheme.colors.textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            false -> {
                                Text(
                                    modifier = Modifier
                                        .weight(1f),
                                    text = "Расписание ещё не составлено",
                                    color = TrackerNewTheme.colors.textColor,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Icon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    component.onEditWeeksClicked()
                                },
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = TrackerNewTheme.colors.tintColor
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = TrackerNewTheme.colors.linearGradientBackground)
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                key = { page -> page }
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        text = days[page].name,
                        color = TrackerNewTheme.colors.textColor,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = days[page].lessons.sortedBy { it.start },
                            key = { it.id }
                        ) {
                            Lesson(
                                lesson = it,
                                onDeleteIconClick = {
                                    component.onDeleteLessonClicked(
                                        state.weeks[page / DAYS_IN_WEEK].id,
                                        it.id
                                    )
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(72.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = TrackerNewTheme.colors.onBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            component.onAddWeekClicked()
                        }
                        .padding(8.dp),
                    text = "Добавить неделю",
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (state.weeks.isNotEmpty()) {
                                val lessons = days[pagerState.currentPage].lessons
                                var futureLessonId = 0
                                if (lessons.isNotEmpty()) {
                                    futureLessonId = lessons.maxOf { it.id } + 1
                                }
                                component.onAddLessonClicked(
                                    state.weeks[pagerState.currentPage / DAYS_IN_WEEK].id,
                                    days[pagerState.currentPage].name,
                                    futureLessonId
                                )
                            }
                        }
                        .padding(8.dp),
                    text = "Добавить занятие",
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 16.sp
                )
            }
        }
    }

}

@Composable
fun Lesson(
    lesson: Lesson,
    onDeleteIconClick: () -> Unit
) {
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                visible = !visible
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = TrackerNewTheme.colors.onBackground
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    text = lesson.name,
                    color = TrackerNewTheme.colors.textColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = lesson.start.toTimeString() + " - " + lesson.end.toTimeString(),
                    color = TrackerNewTheme.colors.textColor,
                )
            }
            AnimatedVisibility(visible = visible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = lesson.lecturer,
                        color = TrackerNewTheme.colors.textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = lesson.audience,
                        color = TrackerNewTheme.colors.textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lesson.typeOfLesson,
                            color = TrackerNewTheme.colors.textColor
                        )
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onDeleteIconClick()
                                },
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = TrackerNewTheme.colors.tintColor
                        )
                    }
                }
            }
        }
    }
}

private const val DAYS_IN_WEEK = 7
private const val ZERO = 0