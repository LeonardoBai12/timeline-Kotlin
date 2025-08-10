package com.airtable.interview.airtableschedule.timeline.presentation.model

import io.lb.schedule.model.Event

data class TimelineState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
