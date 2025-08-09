package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.presentation.model.TimelineUiState
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import io.lb.schedule.model.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state of the timeline screen.
 */
class TimelineViewModel: ViewModel() {
    private val eventDataRepository: EventDataRepository = EventDataRepositoryImpl()

    private val _uiState = MutableStateFlow(TimelineUiState(isLoading = true))
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            delay(1500)

            eventDataRepository.getTimelineItems()
                .collect { events ->
                    _uiState.value = TimelineUiState(
                        events = events,
                        isLoading = false
                    )
                }
        }
    }

    fun deleteEvent(eventId: Int) {
        val currentEvents = _uiState.value.events
        val updatedEvents = currentEvents.filter { it.id != eventId }
        _uiState.value = _uiState.value.copy(events = updatedEvents)
    }

    fun updateEvent(updatedEvent: Event) {
        val currentEvents = _uiState.value.events.toMutableList()
        val eventIndex = currentEvents.indexOfFirst { it.id == updatedEvent.id }

        if (eventIndex != -1) {
            currentEvents[eventIndex] = updatedEvent
            _uiState.value = _uiState.value.copy(events = currentEvents)
        }
    }

    fun refreshEvents() {
        loadEvents()
    }
}
