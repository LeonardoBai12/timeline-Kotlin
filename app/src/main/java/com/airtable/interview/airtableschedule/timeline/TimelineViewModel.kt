package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.models.TimelineEvent
import com.airtable.interview.airtableschedule.models.TimelineState
import com.airtable.interview.airtableschedule.models.TimelineUiEvent
import com.airtable.interview.airtableschedule.presentation.model.TimelineUiState
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.schedule.domain.usecase.EventUseCases
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of the timeline screen.
 */
@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val eventUseCases: EventUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TimelineState(isLoading = true))
    val state: StateFlow<TimelineState> = _state.asStateFlow()

    private val _uiEvent = Channel<TimelineUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getEventsJob: Job? = null

    init {
        onEvent(TimelineEvent.LoadEvents)
    }

    fun onEvent(event: TimelineEvent) {
        when (event) {
            is TimelineEvent.LoadEvents -> loadEvents()
            is TimelineEvent.AddEvent -> addEvent(event.event)
            is TimelineEvent.UpdateEvent -> updateEvent(event.event)
            is TimelineEvent.DeleteEvent -> deleteEvent(event.eventId)
            is TimelineEvent.SearchEvents -> searchEvents(event.query)
            is TimelineEvent.RefreshEvents -> refreshEvents()
        }
    }

    private fun loadEvents() {
        getEventsJob?.cancel()
        getEventsJob = eventUseCases.getAllEventsUseCase()
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                events = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, error = resource.message)
                        }
                        sendUiEvent(TimelineUiEvent.ShowError(resource.message ?: "Unknown error"))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun addEvent(event: io.lb.schedule.model.Event) {
        viewModelScope.launch {
            eventUseCases.insertEventUseCase(event)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowSuccess("Event added successfully"))
                            sendUiEvent(TimelineUiEvent.EventSaved)
                            loadEvents()
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowError(resource.message ?: "Failed to add event"))
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun updateEvent(event: io.lb.schedule.model.Event) {
        viewModelScope.launch {
            eventUseCases.updateEventUseCase(event)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowSuccess("Event updated successfully"))
                            sendUiEvent(TimelineUiEvent.EventSaved)
                            val currentEvents = _state.value.events.toMutableList()
                            val eventIndex = currentEvents.indexOfFirst { it.id == event.id }
                            if (eventIndex != -1) {
                                currentEvents[eventIndex] = event
                                _state.update { it.copy(events = currentEvents) }
                            }
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowError(resource.message ?: "Failed to update event"))
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            eventUseCases.deleteEventUseCase(eventId)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowSuccess("Event deleted successfully"))
                            sendUiEvent(TimelineUiEvent.EventDeleted)
                            val currentEvents = _state.value.events.filter { it.id != eventId }
                            _state.update { it.copy(events = currentEvents) }
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            sendUiEvent(TimelineUiEvent.ShowError(resource.message ?: "Failed to delete event"))
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun searchEvents(query: String) {
        _state.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            loadEvents()
            return
        }

        getEventsJob?.cancel()
        getEventsJob = eventUseCases.searchEventsUseCase(query)
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                events = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, error = resource.message)
                        }
                        sendUiEvent(TimelineUiEvent.ShowError(resource.message ?: "Search failed"))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun refreshEvents() {
        _state.update { it.copy(searchQuery = "") }
        loadEvents()
    }

    private fun sendUiEvent(event: TimelineUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

