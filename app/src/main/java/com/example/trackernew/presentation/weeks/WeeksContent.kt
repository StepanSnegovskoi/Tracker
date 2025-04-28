package com.example.trackernew.presentation.weeks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.presentation.schedule.Lesson
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeksContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
//            items(
//                items = weeks,
//                key = { it.id }
//            ) {
//                Week(
//                    week = it,
//                    onClick = {
//
//                    }
//                )
//            }
        }
    }
}

@Composable
fun Week(
    week: Week,
    onClick: () -> Unit
) {
    val color = (if (week.isActive) Green else Red).copy(alpha = 0.3f)
    val elevation = if (week.isActive) 4.dp else 1.dp
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .background(color)
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = week.name, fontWeight = FontWeight.Bold)
            if(week.isActive){
                Text(text = week.position.toString(), fontWeight = FontWeight.Bold)
            }
        }
    }
}