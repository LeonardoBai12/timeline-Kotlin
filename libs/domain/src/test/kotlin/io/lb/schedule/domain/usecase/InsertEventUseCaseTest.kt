package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class InsertEventUseCaseTest {

    private lateinit var insertEventUseCase: InsertEventUseCase
    private lateinit var repository: EventRepository

    private val testDate = Date(1640995200000)
    private val sampleEvent = Event(0, testDate, testDate, "New Event")

    @Before
    fun setUp() {
        repository = mockk()
        insertEventUseCase = InsertEventUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return Loading then Success when insert succeeds`() = runTest {
        // Given
        val expectedId = 4L
        coEvery { repository.insertEvent(sampleEvent) } returns expectedId

        // When
        val result = insertEventUseCase(sampleEvent).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(expectedId, result[1].data)
        
        coVerify { repository.insertEvent(sampleEvent) }
    }

    @Test
    fun `invoke should return Loading then Error when insert fails`() = runTest {
        // Given
        val exception = RuntimeException("Insert failed")
        coEvery { repository.insertEvent(sampleEvent) } throws exception

        // When
        val result = insertEventUseCase(sampleEvent).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Insert failed", result[1].message)
        
        coVerify { repository.insertEvent(sampleEvent) }
    }

    @Test
    fun `invoke should handle unknown exceptions with default message`() = runTest {
        // Given
        val exception = RuntimeException() // No message
        coEvery { repository.insertEvent(sampleEvent) } throws exception

        // When
        val result = insertEventUseCase(sampleEvent).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Unknown error occurred", result[1].message)
        
        coVerify { repository.insertEvent(sampleEvent) }
    }
}
