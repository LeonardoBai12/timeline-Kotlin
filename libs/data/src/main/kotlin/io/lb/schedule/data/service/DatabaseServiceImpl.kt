package io.lb.schedule.data.service

import io.lb.schedule.data.dao.EventDao
import io.lb.schedule.data.model.EventEntity
import io.lb.schedule.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DatabaseServiceImpl @Inject constructor(
    private val dao: EventDao
) : DatabaseService {
    override fun getAllEvents(): Flow<List<Event>> {
        return dao.getAllEvents().map { entities ->
            entities.map { it.toEvent() }
        }
    }

    override suspend fun insertEvent(event: Event): Long {
        return dao.insertEvent(EventEntity.fromEvent(event))
    }

    override suspend fun updateEvent(event: Event) {
        dao.updateEvent(EventEntity.fromEvent(event))
    }

    override suspend fun deleteEvent(eventId: Int) {
        dao.deleteEventById(eventId)
    }

    override suspend fun getEventById(eventId: Int): Event? {
        return dao.getEventById(eventId)?.toEvent()
    }

    override fun searchEvents(query: String): Flow<List<Event>> {
        return dao.searchEvents(query).map { entities ->
            entities.map { it.toEvent() }
        }
    }
}
