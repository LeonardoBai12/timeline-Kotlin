package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class EventUseCasesTest {

    @Test
    fun `EventUseCases should contain all required use cases`() {
        // Given
        val repository = mockk<EventRepository>()
        
        // When
        val eventUseCases = EventUseCases(
            getAllEventsUseCase = GetAllEventsUseCase(repository),
            insertEventUseCase = InsertEventUseCase(repository),
            updateEventUseCase = UpdateEventUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository),
            searchEventsUseCase = SearchEventsUseCase(repository)
        )

        // Then
        assertNotNull(eventUseCases.getAllEventsUseCase)
        assertNotNull(eventUseCases.insertEventUseCase)
        assertNotNull(eventUseCases.updateEventUseCase)
        assertNotNull(eventUseCases.deleteEventUseCase)
        assertNotNull(eventUseCases.searchEventsUseCase)
    }
}
