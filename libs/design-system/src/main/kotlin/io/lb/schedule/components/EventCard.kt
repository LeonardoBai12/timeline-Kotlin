package io.lb.schedule.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.lb.schedule.model.Event

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
