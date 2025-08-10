package com.aittable.interview.airtableschedule.timeline.presentation

import com.airtable.interview.airtableschedule.timeline.presentation.TimelineViewModel
import com.airtable.interview.airtableschedule.timeline.presentation.model.TimelineEvent
import com.airtable.interview.airtableschedule.timeline.presentation.model.TimelineUiEvent
import io.lb.schedule.domain.usecase.DeleteEventUseCase
import io.lb.schedule.domain.usecase.EventUseCases
import io.lb.schedule.domain.usecase.GetAllEventsUseCase
import io.lb.schedule.domain.usecase.InsertEventUseCase
import io.lb.schedule.domain.usecase.SearchEventsUseCase
import io.lb.schedule.domain.usecase.UpdateEventUseCase
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class TimelineViewModelTest {

    private lateinit var viewModel: TimelineViewModel
    private lateinit var getAllEventsUseCase: GetAllEventsUseCase
    private lateinit var insertEventUseCase: InsertEventUseCase
    private lateinit var updateEventUseCase: UpdateEventUseCase
    private lateinit var deleteEventUseCase: DeleteEventUseCase
    private lateinit var searchEventsUseCase: SearchEventsUseCase
    private lateinit var eventUseCases: EventUseCases

    private val testDispatcher = UnconfinedTestDispatcher()

    private val sampleEvents = listOf(
        Event(1, Date(), Date(), "Event 1"),
        Event(2, Date(), Date(), "Event 2"),
        Event(3, Date(), Date(), "Event 3")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getAllEventsUseCase = mockk(relaxed = true)
        insertEventUseCase = mockk(relaxed = true)
        updateEventUseCase = mockk(relaxed = true)
        deleteEventUseCase = mockk(relaxed = true)
        searchEventsUseCase = mockk(relaxed = true)

        eventUseCases = EventUseCases(
            getAllEventsUseCase = getAllEventsUseCase,
            insertEventUseCase = insertEventUseCase,
            updateEventUseCase = updateEventUseCase,
            deleteEventUseCase = deleteEventUseCase,
            searchEventsUseCase = searchEventsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `init should load events successfully`() = runTest {
        // Given
        every { getAllEventsUseCase() } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(sampleEvents))
        }

        // When
        viewModel = TimelineViewModel(eventUseCases)

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(sampleEvents, state.events)
        assertNull(state.error)

        verify { getAllEventsUseCase() }
    }

    @Test
    fun `init should handle error when loading events fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        every { getAllEventsUseCase() } returns flow {
            emit(Resource.Loading())
            emit(Resource.Error(errorMessage))
        }

        // When
        viewModel = TimelineViewModel(eventUseCases)

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(emptyList<Event>(), state.events)
        assertEquals(errorMessage, state.error)

        verify { getAllEventsUseCase() }
    }

    @Test
    fun `onEvent AddEvent should add event successfully`() = runTest {
        // Given
        val newEvent = Event(0, Date(), Date(), "New Event")
        val eventId = 4L

        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }
        every { insertEventUseCase(newEvent) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(eventId))
        }

        viewModel = TimelineViewModel(eventUseCases)

        // When
        viewModel.onEvent(TimelineEvent.AddEvent(newEvent))

        // Then
        val uiEvents = mutableListOf<TimelineUiEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.toList(uiEvents)
        }

        testScheduler.advanceUntilIdle()

        assertTrue(uiEvents.any { it is TimelineUiEvent.ShowSuccess })
        assertTrue(uiEvents.any { it is TimelineUiEvent.EventSaved })

        verify { insertEventUseCase(newEvent) }
        verify(atLeast = 2) { getAllEventsUseCase() }

        job.cancel()
    }

    @Test
    fun `onEvent UpdateEvent should update event successfully`() = runTest {
        // Given
        val updatedEvent = Event(1, Date(), Date(), "Updated Event")

        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }
        every { updateEventUseCase(updatedEvent) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(Unit))
        }

        viewModel = TimelineViewModel(eventUseCases)

        // When
        viewModel.onEvent(TimelineEvent.UpdateEvent(updatedEvent))

        // Then
        val uiEvents = mutableListOf<TimelineUiEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.toList(uiEvents)
        }

        testScheduler.advanceUntilIdle()

        assertTrue(uiEvents.any { it is TimelineUiEvent.ShowSuccess })
        assertTrue(uiEvents.any { it is TimelineUiEvent.EventSaved })

        val state = viewModel.state.value
        assertTrue(state.events.any { it.id == updatedEvent.id && it.name == updatedEvent.name })

        verify { updateEventUseCase(updatedEvent) }

        job.cancel()
    }

    @Test
    fun `onEvent DeleteEvent should delete event successfully`() = runTest {
        // Given
        val eventIdToDelete = 1

        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }
        every { deleteEventUseCase(eventIdToDelete) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(Unit))
        }

        viewModel = TimelineViewModel(eventUseCases)

        // When
        viewModel.onEvent(TimelineEvent.DeleteEvent(eventIdToDelete))

        // Then
        val uiEvents = mutableListOf<TimelineUiEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.toList(uiEvents)
        }

        testScheduler.advanceUntilIdle()

        assertTrue(uiEvents.any { it is TimelineUiEvent.ShowSuccess })
        assertTrue(uiEvents.any { it is TimelineUiEvent.EventDeleted })

        val state = viewModel.state.value
        assertFalse(state.events.any { it.id == eventIdToDelete })

        verify { deleteEventUseCase(eventIdToDelete) }

        job.cancel()
    }

    @Test
    fun `onEvent SearchEvents should search events successfully`() = runTest {
        // Given
        val query = "Event"
        val searchResults = listOf(sampleEvents[0])

        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }
        every { searchEventsUseCase(query) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(searchResults))
        }

        viewModel = TimelineViewModel(eventUseCases)

        // When
        viewModel.onEvent(TimelineEvent.SearchEvents(query))

        // Then
        val state = viewModel.state.value
        assertEquals(query, state.searchQuery)
        assertEquals(searchResults, state.events)

        verify { searchEventsUseCase(query) }
    }

    @Test
    fun `onEvent SearchEvents with empty query should load all events`() = runTest {
        // Given
        val query = ""

        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }

        viewModel = TimelineViewModel(eventUseCases)

        // When
        viewModel.onEvent(TimelineEvent.SearchEvents(query))

        // Then
        val state = viewModel.state.value
        assertEquals(query, state.searchQuery)

        verify(atLeast = 2) { getAllEventsUseCase() }
    }

    @Test
    fun `onEvent RefreshEvents should refresh events and clear search`() = runTest {
        // Given
        every { getAllEventsUseCase() } returns flow { emit(Resource.Success(sampleEvents)) }

        viewModel = TimelineViewModel(eventUseCases)

        viewModel.onEvent(TimelineEvent.SearchEvents("test"))

        // When
        viewModel.onEvent(TimelineEvent.RefreshEvents)

        // Then
        val state = viewModel.state.value
        assertEquals("", state.searchQuery)
        assertEquals(sampleEvents, state.events)

        verify(atLeast = 2) { getAllEventsUseCase() }
    }
}
