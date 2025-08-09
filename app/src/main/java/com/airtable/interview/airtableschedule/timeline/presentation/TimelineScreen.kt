package com.airtable.interview.airtableschedule.timeline.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airtable.interview.airtableschedule.timeline.presentation.model.TimelineEvent
import com.airtable.interview.airtableschedule.timeline.presentation.model.TimelineState
import com.airtable.interview.airtableschedule.timeline.presentation.model.TimelineUiEvent
import io.lb.schedule.components.DefaultSearchBar
import io.lb.schedule.components.EventAppBar
import io.lb.schedule.components.EventEditBottomSheet
import io.lb.schedule.components.TimelineShimmerView
import io.lb.schedule.components.TimelineView
import io.lb.schedule.model.Event
import io.lb.schedule.ui.theme.ScheduleTheme
import io.lb.schedule.util.filterBy
import io.lb.schedule.util.showToast
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Pure Timeline screen following your MVVM-VI pattern.
 *
 * @param state Current UI state
 * @param eventFlow Flow of UI events from ViewModel
 * @param onEvent Function to send events to ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TimelineScreen(
    state: TimelineState,
    eventFlow: Flow<TimelineUiEvent>,
    onEvent: (TimelineEvent) -> Unit
) {
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    var showEditBottomSheet by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is TimelineUiEvent.ShowError -> {
                    context.showToast(event.message)
                }
                is TimelineUiEvent.ShowSuccess -> {
                    context.showToast(event.message)
                }
                is TimelineUiEvent.EventSaved -> {
                    // Event was saved successfully
                }
                is TimelineUiEvent.EventDeleted -> {
                    // Event was deleted successfully
                }
            }
        }
    }

    val filteredEvents = remember(state.events, searchQuery.value) {
        if (searchQuery.value.isEmpty()) {
            state.events
        } else {
            state.events.filterBy(searchQuery.value)
        }
    }

    ScheduleTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        selectedEvent = Event(
                            id = 0,
                            name = "",
                            startDate = Date(),
                            endDate = Date()
                        )
                        showEditBottomSheet = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Event"
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                EventAppBar(title = "Event Timeline")

                DefaultSearchBar(
                    search = searchQuery,
                    modifier = Modifier.padding(16.dp),
                    hint = "Search events...",
                    onSearch = { query ->
                        // Send search event to ViewModel
                        onEvent(TimelineEvent.SearchEvents(query))
                    },
                    isEnabled = !state.isLoading
                )

                when {
                    state.isLoading -> {
                        TimelineShimmerView(modifier = Modifier.fillMaxSize())
                    }
                    filteredEvents.isNotEmpty() -> {
                        TimelineView(
                            events = filteredEvents,
                            onEventDelete = { event ->
                                onEvent(TimelineEvent.DeleteEvent(event.id))
                            },
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
        }

        if (showEditBottomSheet && selectedEvent != null) {
            EventEditBottomSheet(
                event = selectedEvent!!,
                onDismiss = {
                    showEditBottomSheet = false
                    selectedEvent = null
                },
                onSaveEvent = { updatedEvent ->
                    if (updatedEvent.id == 0) {
                        onEvent(TimelineEvent.AddEvent(updatedEvent))
                    } else {
                        onEvent(TimelineEvent.UpdateEvent(updatedEvent))
                    }
                    showEditBottomSheet = false
                    selectedEvent = null
                }
            )
        }

        state.error?.let { error ->
            LaunchedEffect(error) {
                context.showToast(error)
            }
        }
    }
}
