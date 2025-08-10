package io.lb.schedule.domain.usecase

import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(eventId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        runCatching {
            repository.deleteEvent(eventId)
            emit(Resource.Success(Unit))
        }.getOrElse { exception ->
            emit(Resource.Error(exception.message ?: "Unknown error occurred"))
        }
    }
}
