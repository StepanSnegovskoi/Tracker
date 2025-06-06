package com.example.trackernew.presentation.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Day
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.presentation.extensions.getMinAndMaxTimeString
import com.example.trackernew.presentation.extensions.toTimeString
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green300
import com.example.trackernew.ui.theme.TrackerNewTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar


private const val DAYS_IN_WEEK = 7
private const val ZERO = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleContent(component: ScheduleComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                is ScheduleStore.Label.ClickAddLesson -> {
                    /** Nothing **/
                }

                ScheduleStore.Label.ClickAddWeek -> {
                    /** Nothing **/
                }

                ScheduleStore.Label.ClickEditWeeks -> {
                    /** Nothing **/
                }

                ScheduleStore.Label.ClickSettings -> {
                    /** Nothing **/
                }

                ScheduleStore.Label.DaysListIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.current_list_of_days_is_empty))
                }

                ScheduleStore.Label.AddLessonClickedAndWeeksAreEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.day_of_week_is_not_selected))
                }
            }
        }.launchIn(scope)
    }

    val pagerState = rememberPagerState(
        pageCount = { state.weeks.size * DAYS_IN_WEEK },
        initialPage = ZERO
    )

    LaunchedEffect(state.weeks.size) {
        val newPageCount = state.weeks.size * DAYS_IN_WEEK
        if (newPageCount == 0) return@LaunchedEffect
        if (pagerState.currentPage >= newPageCount) {
            pagerState.scrollToPage(newPageCount - 1)
        }
    }

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
                    TopAppBarContent(
                        state = state,
                        pagerState = pagerState,
                        component = component
                    )
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
            HorizontalPagerLessons(
                pagerState = pagerState,
                days = days,
                state = state,
                onDeleteIconClick = { lessonId, page ->
                    component.onDeleteLessonClicked(
                        state.weeks[page / DAYS_IN_WEEK].id,
                        lessonId
                    )
                }
            )

            BottomBar(
                modifier = Modifier
                    .padding(8.dp),
                component = component,
                state = state,
                pagerState = pagerState,
                moveToCurrentDayClicked = {
                    when (state.weeks.isNotEmpty()) {
                        true -> {
                            scope.launch {
                                val currentDayIndex =
                                    state.weeks.size / 2 * DAYS_IN_WEEK + Calendar.getInstance()
                                        .get(Calendar.DAY_OF_WEEK) - 1

                                pagerState.animateScrollToPage(currentDayIndex - 1)
                            }
                        }

                        false -> {
                            component.onNavigateToCurrentDayClickedAndDaysListIsEmpty()
                        }
                    }
                },
                addLessonClicked = {
                    when (state.weeks.isNotEmpty()) {
                        true -> {
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

                        false -> {
                            component.onAddLessonClickedAndWeeksAreEmpty()
                        }
                    }
                },
                days = days
            )
        }
    }
}

@Composable
private fun HorizontalPagerLessons(
    pagerState: PagerState,
    days: List<Day>,
    state: ScheduleStore.State,
    onDeleteIconClick: (Int, Int) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        key = { page -> page }
    ) { page ->
        if (days.isEmpty()) return@HorizontalPager

        val color =
            if (pagerState.currentPage == state.weeks.size / 2 * DAYS_IN_WEEK + Calendar.getInstance()
                    .get(Calendar.DAY_OF_WEEK) - 1 - 1
            ) Green300 else TrackerNewTheme.colors.textColor

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = days[page].name,
                color = color,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            when (days.isNotEmpty() && days[page].lessons.isNotEmpty()) {
                true -> {
                    val lessons = days[page].lessons.sortedBy { it.start }
                    Text(
                        modifier = Modifier
                            .padding(4.dp),
                        text = lessons.getMinAndMaxTimeString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

                false -> {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(R.string.no_classes),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }

            Lessons(
                days = days,
                page = page,
                onDeleteIconClick = { lessonId ->
                    onDeleteIconClick(lessonId, page)
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.Lessons(
    modifier: Modifier = Modifier,
    days: List<Day>,
    page: Int,
    onDeleteIconClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = days[page].lessons.sortedBy { it.start },
            key = { it.id }
        ) {
            Lesson(
                modifier = Modifier
                    .animateItem(),
                lesson = it,
                onDeleteIconClick = {
                    onDeleteIconClick(it.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun TopAppBarContent(
    modifier: Modifier = Modifier,
    state: ScheduleStore.State,
    pagerState: PagerState,
    component: ScheduleComponent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (state.weeks.isNotEmpty() && pagerState.currentPage / DAYS_IN_WEEK < state.weeks.size) {
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
                    text = stringResource(R.string.schedule_has_not_been_made),
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 18.sp
                )
            }
        }
        Row {
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

            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        component.onSettingsClicked()
                    },
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        }
    }
}

@Composable
private fun BoxScope.BottomBar(
    modifier: Modifier = Modifier,
    component: ScheduleComponent,
    state: ScheduleStore.State,
    pagerState: PagerState,
    moveToCurrentDayClicked: () -> Unit,
    addLessonClicked: () -> Unit,
    days: List<Day>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp)
            .background(
                color = TrackerNewTheme.colors.onBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
            text = stringResource(R.string.add_week),
            color = TrackerNewTheme.colors.textColor,
            fontSize = 16.sp
        )

        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    moveToCurrentDayClicked()
                },
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = if (pagerState.currentPage == state.weeks.size / 2 * DAYS_IN_WEEK + Calendar.getInstance()
                    .get(Calendar.DAY_OF_WEEK) - 1 - 1 && days.isNotEmpty()
            ) Green300 else TrackerNewTheme.colors.tintColor
        )

        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    addLessonClicked()
                }
                .padding(8.dp),
            text = stringResource(R.string.add_lesson),
            color = TrackerNewTheme.colors.textColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun Lesson(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    onDeleteIconClick: () -> Unit
) {
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                visible = !visible
            }
            .then(modifier),
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
            AnimatedLessonInfo(
                visible = visible,
                lesson = lesson,
                onDeleteIconClick = onDeleteIconClick
            )
        }
    }
}

@Composable
private fun ColumnScope.AnimatedLessonInfo(
    modifier: Modifier = Modifier,
    visible: Boolean,
    lesson: Lesson,
    onDeleteIconClick: () -> Unit
) {
    AnimatedVisibility(visible = visible) {
        LessonInfo(
            modifier = modifier,
            lesson = lesson,
            onDeleteIconClick = onDeleteIconClick
        )
    }
}

@Composable
private fun LessonInfo(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    onDeleteIconClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
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