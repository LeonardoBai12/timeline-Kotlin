package io.lb.schedule.data.repository

import io.lb.schedule.data.datasource.EventDataSource
import io.lb.schedule.model.Event
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.test.DefaultAsserter.fail

class EventRepositoryImplTest {

    private lateinit var repository: EventRepositoryImpl
    private lateinit var dataSource: EventDataSource

    private val testDate = Date(1640995200000) // 2022-01-01
    private val sampleEvent = Event(1, testDate, testDate, "Test Event")
    private val sampleEvents = listOf(
        Event(1, testDate, testDate, "Event 1"),
        Event(2, testDate, testDate, "Event 2"),
        Event(3, testDate, testDate, "Event 3")
    )

    @Before
    fun setUp() {
        dataSource = mockk()
        repository = EventRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllEvents should delegate to dataSource`() = runTest {
        // Given
        every { dataSource.getAllEvents() } returns flow { emit(sampleEvents) }

        // When
        val result = repository.getAllEvents().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(sampleEvents, result[0])
        
        verify { dataSource.getAllEvents() }
    }

    @Test
    fun `insertEvent should delegate to dataSource and return event id`() = runTest {
        // Given
        val expectedId = 4L
        coEvery { dataSource.insertEvent(sampleEvent) } returns expectedId

        // When
        val result = repository.insertEvent(sampleEvent)

        // Then
        assertEquals(expectedId, result)
        
        coVerify { dataSource.insertEvent(sampleEvent) }
    }

    @Test
    fun `updateEvent should delegate to dataSource`() = runTest {
        // Given
        coEvery { dataSource.updateEvent(sampleEvent) } just Runs

        // When
        repository.updateEvent(sampleEvent)

        // Then
        coVerify { dataSource.updateEvent(sampleEvent) }
    }

    @Test
    fun `deleteEvent should delegate to dataSource`() = runTest {
        // Given
        val eventId = 1
        coEvery { dataSource.deleteEvent(eventId) } just Runs

        // When
        repository.deleteEvent(eventId)

        // Then
        coVerify { dataSource.deleteEvent(eventId) }
    }

    @Test
    fun `getEventById should delegate to dataSource and return event when found`() = runTest {
        // Given
        val eventId = 1
        coEvery { dataSource.getEventById(eventId) } returns sampleEvent

        // When
        val result = repository.getEventById(eventId)

        // Then
        assertEquals(sampleEvent, result)
        
        coVerify { dataSource.getEventById(eventId) }
    }

    @Test
    fun `searchEvents should delegate to dataSource`() = runTest {
        // Given
        val query = "Event 1"
        val filteredEvents = listOf(sampleEvents[0])
        every { dataSource.searchEvents(query) } returns flow { emit(filteredEvents) }

        // When
        val result = repository.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(filteredEvents, result[0])
        
        verify { dataSource.searchEvents(query) }
    }

    @Test
    fun `all methods should properly propagate exceptions from dataSource`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        
        // Test getAllEvents
        every { dataSource.getAllEvents() } throws exception
        
        try {
            repository.getAllEvents().toList()
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }

        // Test insertEvent
        coEvery { dataSource.insertEvent(any()) } throws exception
        
        try {
            repository.insertEvent(sampleEvent)
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }
}
