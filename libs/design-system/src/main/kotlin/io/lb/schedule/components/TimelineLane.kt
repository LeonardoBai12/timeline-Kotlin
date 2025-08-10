package io.lb.schedule.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.schedule.model.Event

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
