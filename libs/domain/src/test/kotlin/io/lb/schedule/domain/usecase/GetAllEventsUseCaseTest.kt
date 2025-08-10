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

class GetAllEventsUseCaseTest {

    private lateinit var getAllEventsUseCase: GetAllEventsUseCase
    private lateinit var repository: EventRepository

    private val testDate = Date(1640995200000)
    private val sampleEvents = listOf(
        Event(1, testDate, testDate, "Event 1"),
        Event(2, testDate, testDate, "Event 2"),
        Event(3, testDate, testDate, "Event 3")
    )

    @Before
    fun setUp() {
        repository = mockk()
        getAllEventsUseCase = GetAllEventsUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return Success resource with events from repository`() = runTest {
        // Given
        every { repository.getAllEvents() } returns flow { emit(sampleEvents) }

        // When
        val result = getAllEventsUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(sampleEvents, result[0].data)
        
        verify { repository.getAllEvents() }
    }

    @Test
    fun `invoke should return Success resource with empty list when repository returns empty`() = runTest {
        // Given
        every { repository.getAllEvents() } returns flow { emit(emptyList()) }

        // When
        val result = getAllEventsUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(emptyList<Event>(), result[0].data)
        
        verify { repository.getAllEvents() }
    }
}
