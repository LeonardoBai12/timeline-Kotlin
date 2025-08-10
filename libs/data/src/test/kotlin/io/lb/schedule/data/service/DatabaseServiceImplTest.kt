package io.lb.schedule.data.service

import io.lb.schedule.data.dao.EventDao
import io.lb.schedule.data.datasource.EventDataSource
import io.lb.schedule.data.model.EventEntity
import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.domain.usecase.DeleteEventUseCase
import io.lb.schedule.domain.usecase.EventUseCases
import io.lb.schedule.domain.usecase.GetAllEventsUseCase
import io.lb.schedule.domain.usecase.InsertEventUseCase
import io.lb.schedule.domain.usecase.SearchEventsUseCase
import io.lb.schedule.domain.usecase.UpdateEventUseCase
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
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
import kotlin.test.DefaultAsserter.fail

class DatabaseServiceImplTest {

    private lateinit var databaseService: DatabaseServiceImpl
    private lateinit var eventDao: EventDao

    private val testDate = Date(1640995200000) // 2022-01-01
    private val sampleEvent = Event(1, testDate, testDate, "Test Event")
    private val sampleEventEntity = EventEntity(1, "Test Event", testDate.time, testDate.time)

    private val sampleEvents = listOf(
        Event(1, testDate, testDate, "Event 1"),
        Event(2, testDate, testDate, "Event 2"),
        Event(3, testDate, testDate, "Event 3")
    )

    private val sampleEventEntities = listOf(
        EventEntity(1, "Event 1", testDate.time, testDate.time),
        EventEntity(2, "Event 2", testDate.time, testDate.time),
        EventEntity(3, "Event 3", testDate.time, testDate.time)
    )

    @Before
    fun setUp() {
        eventDao = mockk()
        databaseService = DatabaseServiceImpl(eventDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllEvents should return flow of events from dao`() = runTest {
        // Given
        every { eventDao.getAllEvents() } returns flow { emit(sampleEventEntities) }

        // When
        val result = databaseService.getAllEvents().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(sampleEvents, result[0])
        
        verify { eventDao.getAllEvents() }
    }

    @Test
    fun `insertEvent should call dao insertEvent and return event id`() = runTest {
        // Given
        val expectedId = 4L
        coEvery { eventDao.insertEvent(any()) } returns expectedId

        // When
        val result = databaseService.insertEvent(sampleEvent)

        // Then
        assertEquals(expectedId, result)
        
        coVerify { 
            eventDao.insertEvent(
                match { entity ->
                    entity.name == sampleEvent.name &&
                    entity.startDate == sampleEvent.startDate.time &&
                    entity.endDate == sampleEvent.endDate.time
                }
            )
        }
    }

    @Test
    fun `updateEvent should call dao updateEvent`() = runTest {
        // Given
        coEvery { eventDao.updateEvent(any()) } just Runs

        // When
        databaseService.updateEvent(sampleEvent)

        // Then
        coVerify { 
            eventDao.updateEvent(
                match { entity ->
                    entity.id == sampleEvent.id &&
                    entity.name == sampleEvent.name &&
                    entity.startDate == sampleEvent.startDate.time &&
                    entity.endDate == sampleEvent.endDate.time
                }
            )
        }
    }

    @Test
    fun `deleteEvent should call dao deleteEventById`() = runTest {
        // Given
        val eventId = 1
        coEvery { eventDao.deleteEventById(eventId) } just Runs

        // When
        databaseService.deleteEvent(eventId)

        // Then
        coVerify { eventDao.deleteEventById(eventId) }
    }

    @Test
    fun `getEventById should return event when found`() = runTest {
        // Given
        val eventId = 1
        coEvery { eventDao.getEventById(eventId) } returns sampleEventEntity

        // When
        val result = databaseService.getEventById(eventId)

        // Then
        assertNotNull(result)
        assertEquals(sampleEvent, result)
        
        coVerify { eventDao.getEventById(eventId) }
    }

    @Test
    fun `getEventById should return null when not found`() = runTest {
        // Given
        val eventId = 999
        coEvery { eventDao.getEventById(eventId) } returns null

        // When
        val result = databaseService.getEventById(eventId)

        // Then
        assertNull(result)
        
        coVerify { eventDao.getEventById(eventId) }
    }

    @Test
    fun `searchEvents should return flow of filtered events from dao`() = runTest {
        // Given
        val query = "Event 1"
        val filteredEntities = listOf(sampleEventEntities[0])
        val expectedEvents = listOf(sampleEvents[0])
        
        every { eventDao.searchEvents(query) } returns flow { emit(filteredEntities) }

        // When
        val result = databaseService.searchEvents(query).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedEvents, result[0])
        
        verify { eventDao.searchEvents(query) }
    }
}
