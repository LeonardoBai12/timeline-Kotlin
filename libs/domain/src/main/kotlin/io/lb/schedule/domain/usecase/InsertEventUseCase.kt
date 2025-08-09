package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import io.lb.schedule.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsertEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(event: Event): Flow<Resource<Long>> = flow {
        emit(Resource.Loading())
        runCatching {
            val eventId = repository.insertEvent(event)
            emit(Resource.Success(eventId))
        }.getOrElse { exception ->
            emit(Resource.Error(exception.message ?: "Unknown error occurred"))
        }
    }
}