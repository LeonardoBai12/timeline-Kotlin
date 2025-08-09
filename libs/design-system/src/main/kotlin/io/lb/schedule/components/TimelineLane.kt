package io.lb.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.schedule.designsystem.R
import io.lb.schedule.model.Event
import io.lb.schedule.util.*

@Composable
fun TimelineLane(
    laneIndex: Int,
    events: List<Event>,
    onEventSwipe: (Event) -> Unit,
    onEventClick: (Event) -> Unit
) {
    Column {
        Text(
            text = "Lane ${laneIndex + 1}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
            events.forEach { event ->
                EventTimelineCard(
                    event = event,
                    onSwipe = { onEventSwipe(event) },
                    onClick = { onEventClick(event) }
                )
        }
    }
}