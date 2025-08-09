package com.airtable.interview.airtableschedule.presentation.model

import io.lb.schedule.model.Event

/**
 * UI state for the timeline screen.
 */
data class TimelineUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
)
