package io.lb.schedule.data.datasource

import io.lb.schedule.data.service.DatabaseService
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
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.test.DefaultAsserter.fail

class EventDataSourceTest {

    private lateinit var dataSource: EventDataSource
    private lateinit var databaseService: DatabaseService

    private val testDate = Date(1640995200000)
    private val sampleEvent = Event(1, testDate, testDate, "Test Event")
    private val sampleEvents = listOf(
        Event(1, testDate, testDate, "Event 1"),
        Event(2, testDate, testDate, "Event 2"),
        Event(3, testDate, testDate, "Event 3")
    )

    @Before
    fun setUp() {
        databaseService = mockk()
        dataSource = EventDataSource(databaseService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllEvents should delegate to databaseService`() = runTest {
        // Given
        every { databaseService.getAllEvents() } returns flow { emit(sampleEvents) }

        // When
        val result = dataSource.getAllEvents().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(sampleEvents, result[0])
        
        verify { databaseService.getAllEvents() }
    }

    @Test
    fun `getAllEvents should return empty list when databaseService returns empty`() = runTest {
        // Given
        every { databaseService.getAllEvents() } returns flow { emit(emptyList()) }

        // When
        val result = dataSource.getAllEvents().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyList<Event>(), result[0])
        
        verify { databaseService.getAllEvents() }
    }

    @Test
    fun `insertEvent should delegate to databaseService and return event id`() = runTest {
        // Given
        val expectedId = 4L
        coEvery { databaseService.insertEvent(sampleEvent) } returns expectedId

        // When
        val result = dataSource.insertEvent(sampleEvent)

        // Then
        assertEquals(expectedId, result)
        
        coVerify { databaseService.insertEvent(sampleEvent) }
    }

    @Test
    fun `insertEvent should handle new event with id 0`() = runTest {
        // Given
        val newEvent = Event(0, testDate, testDate, "New Event")
        val expectedId = 1L
        coEvery { databaseService.insertEvent(newEvent) } returns expectedId

        // When
        val result = dataSource.insertEvent(newEvent)

        // Then
        assertEquals(expectedId, result)
        
        coVerify { databaseService.insertEvent(newEvent) }
    }

    @Test
    fun `updateEvent should delegate to databaseService`() = runTest {
        // Given
        coEvery { databaseService.updateEvent(sampleEvent) } just Runs

        // When
        dataSource.updateEvent(sampleEvent)

        // Then
        coVerify { databaseService.updateEvent(sampleEvent) }
    }

    @Test
    fun `deleteEvent should delegate to databaseService`() = runTest {
        // Given
        val eventId = 1
        coEvery { databaseService.deleteEvent(eventId) } just Runs

        // When
        dataSource.deleteEvent(eventId)

        // Then
        coVerify { databaseService.deleteEvent(eventId) }
    }

    @Test
    fun `getEventById should delegate to databaseService and return event when found`() = runTest {
        // Given
        val eventId = 1
        coEvery { databaseService.getEventById(eventId) } returns sampleEvent

        // When
        val result = dataSource.getEventById(eventId)

        // Then
        assertEquals(sampleEvent, result)
        
        coVerify { databaseService.getEventById(eventId) }
    }

    @Test
    fun `getEventById should delegate to databaseService and return null when not found`() = runTest {
        // Given
        val eventId = 999
        coEvery { databaseService.getEventById(eventId) } returns null

        // When
        val result = dataSource.getEventById(eventId)

        // Then
        assertNull(result)
        
        coVerify { databaseService.getEventById(eventId) }
    }

    @Test
    fun `searchEvents should delegate to databaseService`() = runTest {
        // Given
        val query = "Event 1"
        val filteredEvents = listOf(sampleEvents[0])
        every { databaseService.searchEvents(query) } returns flow { emit(filteredEvents) }

        // When
        val result = dataSource.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(filteredEvents, result[0])
        
        verify { databaseService.searchEvents(query) }
    }

    @Test
    fun `searchEvents should return empty list when no matches found`() = runTest {
        // Given
        val query = "NonExistent"
        every { databaseService.searchEvents(query) } returns flow { emit(emptyList()) }

        // When
        val result = dataSource.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyList<Event>(), result[0])
        
        verify { databaseService.searchEvents(query) }
    }

    @Test
    fun `searchEvents should handle special characters in query`() = runTest {
        // Given
        val query = "Event's & \"Test\""
        val filteredEvents = listOf(sampleEvents[0])
        every { databaseService.searchEvents(query) } returns flow { emit(filteredEvents) }

        // When
        val result = dataSource.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(filteredEvents, result[0])
        
        verify { databaseService.searchEvents(query) }
    }

    @Test
    fun `searchEvents should handle empty query`() = runTest {
        // Given
        val query = ""
        every { databaseService.searchEvents(query) } returns flow { emit(sampleEvents) }

        // When
        val result = dataSource.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(sampleEvents, result[0])
        
        verify { databaseService.searchEvents(query) }
    }

    @Test
    fun `all methods should properly propagate exceptions from databaseService`() = runTest {
        // Given
        val exception = RuntimeException("Database service error")
        
        // Test getAllEvents
        every { databaseService.getAllEvents() } throws exception
        
        try {
            dataSource.getAllEvents().toList()
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }

        // Test insertEvent
        coEvery { databaseService.insertEvent(any()) } throws exception
        
        try {
            dataSource.insertEvent(sampleEvent)
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }

        // Test updateEvent
        coEvery { databaseService.updateEvent(any()) } throws exception
        
        try {
            dataSource.updateEvent(sampleEvent)
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }

        // Test deleteEvent
        coEvery { databaseService.deleteEvent(any()) } throws exception
        
        try {
            dataSource.deleteEvent(1)
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }

        // Test getEventById
        coEvery { databaseService.getEventById(any()) } throws exception
        
        try {
            dataSource.getEventById(1)
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }

        // Test searchEvents
        every { databaseService.searchEvents(any()) } throws exception
        
        try {
            dataSource.searchEvents("test").toList()
            fail("Should have thrown exception")
        } catch (e: RuntimeException) {
            assertEquals("Database service error", e.message)
        }
        
        // Verify all methods were called
        verify { databaseService.getAllEvents() }
        coVerify { databaseService.insertEvent(any()) }
        coVerify { databaseService.updateEvent(any()) }
        coVerify { databaseService.deleteEvent(any()) }
        coVerify { databaseService.getEventById(any()) }
        verify { databaseService.searchEvents(any()) }
    }

    @Test
    fun `getAllEvents should handle multiple emissions from databaseService`() = runTest {
        // Given
        val firstEmission = listOf(sampleEvents[0])
        val secondEmission = sampleEvents
        every { databaseService.getAllEvents() } returns flow {
            emit(firstEmission)
            emit(secondEmission)
        }

        // When
        val result = dataSource.getAllEvents().toList()

        // Then
        assertEquals(2, result.size)
        assertEquals(firstEmission, result[0])
        assertEquals(secondEmission, result[1])
        
        verify { databaseService.getAllEvents() }
    }

    @Test
    fun `searchEvents should handle multiple emissions from databaseService`() = runTest {
        // Given
        val query = "Event"
        val firstEmission = listOf(sampleEvents[0])
        val secondEmission = listOf(sampleEvents[0], sampleEvents[1])
        every { databaseService.searchEvents(query) } returns flow {
            emit(firstEmission)
            emit(secondEmission)
        }

        // When
        val result = dataSource.searchEvents(query).toList()

        // Then
        assertEquals(2, result.size)
        assertEquals(firstEmission, result[0])
        assertEquals(secondEmission, result[1])
        
        verify { databaseService.searchEvents(query) }
    }

    @Test
    fun `insertEvent should preserve exact event data when delegating`() = runTest {
        // Given
        val complexEvent = Event(
            id = 42,
            startDate = Date(1640995200000),
            endDate = Date(1641081600000), // Next day
            name = "Complex Event with Special Characters: @#$%^&*()"
        )
        val expectedId = 42L
        coEvery { databaseService.insertEvent(complexEvent) } returns expectedId

        // When
        val result = dataSource.insertEvent(complexEvent)

        // Then
        assertEquals(expectedId, result)
        
        coVerify { 
            databaseService.insertEvent(
                match { event ->
                    event.id == complexEvent.id &&
                    event.name == complexEvent.name &&
                    event.startDate == complexEvent.startDate &&
                    event.endDate == complexEvent.endDate
                }
            )
        }
    }

    @Test
    fun `updateEvent should preserve exact event data when delegating`() = runTest {
        // Given
        val complexEvent = Event(
            id = 42,
            startDate = Date(1640995200000),
            endDate = Date(1641081600000),
            name = "Updated Complex Event with Unicode: 🎉📅✨"
        )
        coEvery { databaseService.updateEvent(complexEvent) } just Runs

        // When
        dataSource.updateEvent(complexEvent)

        // Then
        coVerify { 
            databaseService.updateEvent(
                match { event ->
                    event.id == complexEvent.id &&
                    event.name == complexEvent.name &&
                    event.startDate == complexEvent.startDate &&
                    event.endDate == complexEvent.endDate
                }
            )
        }
    }
}
