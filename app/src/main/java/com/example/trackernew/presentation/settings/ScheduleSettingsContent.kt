package com.example.trackernew.presentation.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.ui.theme.TrackerNewTheme

@Composable
fun ScheduleSettingsContent(component: ScheduleSettingsComponent) {
    val state = component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrackerNewTheme.colors.background)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val visibleLecturers = remember {
                mutableStateOf(false)
            }
            Section(
                name = stringResource(R.string.lecturers),
                visibleItems = visibleLecturers,
                items = state.value.lecturers.map { it.name },
                onIconDeleteClick = {
                    component.onDeleteLecturerClicked(it)
                },
                onSectionClick = {
                    visibleLecturers.value = !visibleLecturers.value
                }
            )
            Spacer(modifier = Modifier.height(4.dp))

            val visibleLessons = remember {
                mutableStateOf(false)
            }
            Section(
                name = stringResource(R.string.lessons),
                visibleItems = visibleLessons,
                items = state.value.lessonNames.map { it.name },
                onIconDeleteClick = {
                    component.onDeleteLessonNameClicked(it)
                },
                onSectionClick = {
                    visibleLessons.value = !visibleLessons.value
                }
            )
            Spacer(modifier = Modifier.height(4.dp))

            val visibleAudiences = remember {
                mutableStateOf(false)
            }
            Section(
                name = stringResource(R.string.audiences),
                visibleItems = visibleAudiences,
                items = state.value.audiences.map { it.name },
                onIconDeleteClick = {
                    component.onDeleteAudienceClicked(it)
                },
                onSectionClick = {
                    visibleAudiences.value = !visibleAudiences.value
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun Item(
    modifier: Modifier = Modifier,
    name: String,
    onDeleteIconClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = CardDefaults.cardColors()
            .copy(containerColor = TrackerNewTheme.colors.onBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = TrackerNewTheme.colors.textColor
            )

            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            onDeleteIconClick()
                        }
                    ),
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        }
    }
}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    name: String,
    visibleItems: State<Boolean>,
    items: List<String>,
    onIconDeleteClick: (String) -> Unit,
    onSectionClick: () -> Unit
) {
    val lineColor = TrackerNewTheme.colors.oppositeColor
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        onSectionClick()
                    }
                )
                .then(modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                color = TrackerNewTheme.colors.textColor
            )
            val icon = when(visibleItems.value){
                true -> Icons.Default.KeyboardArrowDown
                false -> Icons.Default.KeyboardArrowUp
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        }

        val transitionState = remember { MutableTransitionState(false) }
        transitionState.targetState = visibleItems.value

        AnimatedVisibility(visibleState = transitionState) {
            LazyColumn(
                modifier = Modifier
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = items,
                    key = { it }
                ) {
                    Item(
                        modifier = Modifier
                            .animateItem(),
                        name = it,
                        onDeleteIconClick = {
                            onIconDeleteClick(it)
                        }
                    )
                }
            }
        }
    }
}