package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class SearchEventsUseCaseTest {

    private lateinit var searchEventsUseCase: SearchEventsUseCase
    private lateinit var repository: EventRepository

    private val testDate = Date(1640995200000)
    private val sampleEvents = listOf(
        Event(1, testDate, testDate, "Event 1"),
        Event(2, testDate, testDate, "Event 2"),
        Event(3, testDate, testDate, "Meeting 1")
    )

    @Before
    fun setUp() {
        repository = mockk()
        searchEventsUseCase = SearchEventsUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return Success resource with filtered events from repository`() = runTest {
        // Given
        val query = "Event"
        val filteredEvents = listOf(sampleEvents[0], sampleEvents[1])
        every { repository.searchEvents(query) } returns flow { emit(filteredEvents) }

        // When
        val result = searchEventsUseCase(query).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(filteredEvents, result[0].data)
        
        verify { repository.searchEvents(query) }
    }

    @Test
    fun `invoke should return Success resource with empty list when no matches found`() = runTest {
        // Given
        val query = "NonExistent"
        every { repository.searchEvents(query) } returns flow { emit(emptyList()) }

        // When
        val result = searchEventsUseCase(query).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(emptyList<Event>(), result[0].data)
        
        verify { repository.searchEvents(query) }
    }

    @Test
    fun `invoke should handle empty query`() = runTest {
        // Given
        val query = ""
        every { repository.searchEvents(query) } returns flow { emit(sampleEvents) }

        // When
        val result = searchEventsUseCase(query).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(sampleEvents, result[0].data)
        
        verify { repository.searchEvents(query) }
    }
}
