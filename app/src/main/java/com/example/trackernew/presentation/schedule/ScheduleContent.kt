package com.example.trackernew.presentation.schedule

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trackernew.ui.theme.TrackerNewTheme

@Composable
@Preview
fun ScheduleContent() {
    Scaffold (
        containerColor = TrackerNewTheme.colors.background
    ) { paddingValues ->
        paddingValues
    }
}