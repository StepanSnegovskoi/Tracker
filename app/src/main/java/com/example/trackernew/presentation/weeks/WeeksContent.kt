package com.example.trackernew.presentation.weeks

import android.icu.util.Calendar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.ui.theme.TrackerNewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeksContent(component: WeeksComponent) {
    val state = component.model.collectAsState()

    val calendar = Calendar.getInstance()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TrackerNewTheme.colors.background
                ),
                title = {
                    TopAppBarContent(calendar = calendar)
                }
            )
        },
        floatingActionButton = {
            FAB(component)
        },
        containerColor = TrackerNewTheme.colors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = TrackerNewTheme.colors.linearGradientBackground)
        ) {
            Weeks(
                paddingValues,
                state,
                onLongClick = {
                    component.onWeekStatusChanged(it)
                },
                onDeleteWeekClick = {
                    component.onDeleteWeekClicked(it)
                },
                onMoveUpWeekClick = {
                    component.onMoveUpWeekClicked(it)
                },
                onMoveDownWeekClick = {
                    component.onMoveDownWeekClicked(it)
                },
                onSelectWeekAsCurrentClick = {
                    component.onSelectWeekAsCurrentClicked(it)
                },
            )
        }
    }
}

@Composable
private fun Weeks(
    paddingValues: PaddingValues,
    state: State<WeeksStore.State>,
    onLongClick: (Week) -> Unit,
    onDeleteWeekClick: (Int) -> Unit,
    onMoveUpWeekClick: (Week) -> Unit,
    onMoveDownWeekClick: (Week) -> Unit,
    onSelectWeekAsCurrentClick: (Week) -> Unit

) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        itemsIndexed(
            items = state.value.weeks,
            key = { _, item -> item.id }
        ) { index, week ->
            Week(
                week = week,
                weekIndex = index,
                weeksSize = state.value.weeks.size,
                onLongClick = {
                    onLongClick(week)
                },
                onDeleteIconClick = {
                    onDeleteWeekClick(week.id)
                },
                onMoveUpIconClick = {
                    onMoveUpWeekClick(week)
                },
                onMoveDownIconClick = {
                    onMoveDownWeekClick(week)
                },
                onClick = {
                    onSelectWeekAsCurrentClick(week)
                }
            )
        }
    }
}

@Composable
private fun FAB(
    component: WeeksComponent,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = {
            component.onConfirmEditClicked()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = TrackerNewTheme.colors.onBackground
        )
    ) {
        Text(
            text = stringResource(R.string.confirm),
            color = TrackerNewTheme.colors.textColor
        )
    }
}

@Composable
private fun TopAppBarContent(
    modifier: Modifier = Modifier,
    calendar: Calendar
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.weeks),
            color = TrackerNewTheme.colors.textColor
        )
        Text(
            modifier = Modifier
                .padding(end = 24.dp),
            text = stringResource(
                R.string.current_week_of_year,
                calendar.get(Calendar.WEEK_OF_YEAR)
            ),
            fontSize = 14.sp,
            color = TrackerNewTheme.colors.textColor
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Week(
    modifier: Modifier = Modifier,
    week: Week,
    weekIndex: Int,
    weeksSize: Int,
    onLongClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onMoveUpIconClick: () -> Unit,
    onMoveDownIconClick: () -> Unit,
    onClick: () -> Unit
) {

    val color = if (week.selectedAsCurrent) {
        TrackerNewTheme.colors.lightGreen
    } else if (week.isActive) {
        TrackerNewTheme.colors.darkGreen
    } else {
        TrackerNewTheme.colors.red
    }

    val elevation = if (week.isActive) 4.dp else 1.dp

    Card(
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .then(modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = week.name,
                fontWeight = FontWeight.Bold,
                color = TrackerNewTheme.colors.textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (week.selectedAsCurrent) {
                Text(
                    text = stringResource(R.string.week_of_year, week.weekOfYear),
                    fontSize = 12.sp,
                    color = TrackerNewTheme.colors.textColor
                )
            }
            if (weeksSize > 1) {
                if (weekIndex != 0 && weekIndex != weeksSize - 1) {
                    Column {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onMoveUpIconClick()
                                },
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = TrackerNewTheme.colors.tintColor
                        )
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onMoveDownIconClick()
                                },
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TrackerNewTheme.colors.tintColor
                        )
                    }
                } else if (weekIndex == weeksSize - 1) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                onMoveUpIconClick()
                            },
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = TrackerNewTheme.colors.tintColor
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                onMoveDownIconClick()
                            },
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TrackerNewTheme.colors.tintColor
                    )
                }
            }
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