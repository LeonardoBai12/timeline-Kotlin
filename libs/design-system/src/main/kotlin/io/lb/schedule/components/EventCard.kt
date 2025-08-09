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
fun TimelineView(
    events: List<Event>,
    onEventDelete: (Event) -> Unit,
    onEventClick: (Event) -> Unit,
) {
    if (events.isEmpty()) return
    val lanes = remember(events) { assignLanes(events) }

    LazyColumn {
        items(lanes.size) { laneIndex ->
            val laneEvents = lanes[laneIndex]

            TimelineLane(
                laneIndex = laneIndex,
                events = laneEvents,
                onEventSwipe = onEventDelete,
                onEventClick = onEventClick
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun assignLanes(events: List<Event>): List<List<Event>> {
    val lanes = mutableListOf<MutableList<Event>>()

    events.sortedBy { event -> event.startDate }
        .forEach { event ->
            val availableLane = lanes.find { lane ->
                lane.last().endDate < event.startDate
            }
            if (availableLane != null) {
                availableLane.add(event)
            } else {
                lanes.add(mutableListOf(event))
            }
        }
    return lanes
}
