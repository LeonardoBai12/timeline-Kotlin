package io.lb.schedule.domain.usecase

data class EventUseCases(
    val getAllEventsUseCase: GetAllEventsUseCase,
    val insertEventUseCase: InsertEventUseCase,
    val updateEventUseCase: UpdateEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase,
    val searchEventsUseCase: SearchEventsUseCase
)
