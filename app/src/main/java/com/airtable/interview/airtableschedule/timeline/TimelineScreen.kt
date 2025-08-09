package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.lb.schedule.components.DefaultSearchBar
import io.lb.schedule.components.EventAppBar
import io.lb.schedule.components.EventEditBottomSheet
import io.lb.schedule.components.TimelineShimmerView
import io.lb.schedule.components.TimelineView
import io.lb.schedule.model.Event
import io.lb.schedule.ui.theme.ScheduleTheme
import io.lb.schedule.util.filterBy

/**
 * A screen that displays a timeline of events.
 */
@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = TimelineViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery = remember { mutableStateOf("") }
    var showEditBottomSheet by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    val filteredEvents = remember(uiState.events, searchQuery.value) {
        if (searchQuery.value.isEmpty()) {
            uiState.events
        } else {
            uiState.events.filterBy(searchQuery.value)
        }
    }

    ScheduleTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            EventAppBar(title = "Event Timeline")

            DefaultSearchBar(
                search = searchQuery,
                modifier = Modifier.padding(16.dp),
                hint = "Search events...",
                onSearch = { /* Search is handled by state */ },
                isEnabled = !uiState.isLoading
            )

            when {
                uiState.isLoading -> {
                    TimelineShimmerView(modifier = Modifier.fillMaxSize())
                }
                filteredEvents.isNotEmpty() -> {
                    TimelineView(
                        events = filteredEvents,
                        onEventDelete = { event -> viewModel.deleteEvent(event.id) },
                        onEventClick = { event ->
                            selectedEvent = event
                            showEditBottomSheet = true
                        },
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.value.isEmpty()) "No events available" else "No events match your search",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (showEditBottomSheet && selectedEvent != null) {
            EventEditBottomSheet(
                event = selectedEvent!!,
                onDismiss = {
                    showEditBottomSheet = false
                    selectedEvent = null
                },
                onSaveEvent = { updatedEvent ->
                    viewModel.updateEvent(updatedEvent)
                    showEditBottomSheet = false
                    selectedEvent = null
                }
            )
        }
    }
}
