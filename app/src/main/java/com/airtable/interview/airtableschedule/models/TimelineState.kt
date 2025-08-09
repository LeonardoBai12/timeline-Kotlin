package com.airtable.interview.airtableschedule.models

import io.lb.schedule.model.Event

data class TimelineState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)