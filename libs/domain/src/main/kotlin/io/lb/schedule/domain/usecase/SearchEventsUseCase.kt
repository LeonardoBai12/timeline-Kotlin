package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<Event>>> {
        return repository.searchEvents(query).map { events ->
            Resource.Success(events)
        }
    }
}