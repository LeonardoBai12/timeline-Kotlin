package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
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

class DeleteEventUseCaseTest {

    private lateinit var deleteEventUseCase: DeleteEventUseCase
    private lateinit var repository: EventRepository

    @Before
    fun setUp() {
        repository = mockk()
        deleteEventUseCase = DeleteEventUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should return Loading then Success when delete succeeds`() = runTest {
        // Given
        val eventId = 1
        coEvery { repository.deleteEvent(eventId) } just Runs

        // When
        val result = deleteEventUseCase(eventId).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(Unit, result[1].data)

        coVerify { repository.deleteEvent(eventId) }
    }

    @Test
    fun `invoke should return Loading then Error when delete fails`() = runTest {
        // Given
        val eventId = 1
        val exception = RuntimeException("Delete failed")
        coEvery { repository.deleteEvent(eventId) } throws exception

        // When
        val result = deleteEventUseCase(eventId).toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Delete failed", result[1].message)

        coVerify { repository.deleteEvent(eventId) }
    }
}
