package com.airtable.interview.airtableschedule.models

import io.lb.schedule.model.Event

sealed class TimelineEvent {
    data object LoadEvents : TimelineEvent()
    data class AddEvent(val event: Event) : TimelineEvent()
    data class UpdateEvent(val event: Event) : TimelineEvent()
    data class DeleteEvent(val eventId: Int) : TimelineEvent()
    data class SearchEvents(val query: String) : TimelineEvent()
    data object RefreshEvents : TimelineEvent()
}