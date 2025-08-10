package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class UpdateEventUseCaseTest {

    private lateinit var updateEventUseCase: UpdateEventUseCase
    private lateinit var repository: EventRepository

    private val testDate = Date(1640995200000)
    private val sampleEvent = Event(1, testDate, testDate, "Updated Event")

    @Before
    fun setUp() {
        repository = mockk()
        updateEventUseCase = UpdateEventUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return Loading then Success when update succeeds`() = runTest {
        // Given
        coEvery { repository.updateEvent(sampleEvent) } just Runs

        // When
        val result = updateEventUseCase(sampleEvent).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(Unit, result[1].data)
        
        coVerify { repository.updateEvent(sampleEvent) }
    }

    @Test
    fun `invoke should return Loading then Error when update fails`() = runTest {
        // Given
        val exception = RuntimeException("Update failed")
        coEvery { repository.updateEvent(sampleEvent) } throws exception

        // When
        val result = updateEventUseCase(sampleEvent).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Update failed", result[1].message)
        
        coVerify { repository.updateEvent(sampleEvent) }
    }
}
