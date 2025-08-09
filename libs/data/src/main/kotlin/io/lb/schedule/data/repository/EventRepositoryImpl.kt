package io.lb.schedule.data.repository

import io.lb.schedule.data.datasource.EventDataSource
import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class EventRepositoryImpl @Inject constructor(
    private val dataSource: EventDataSource
) : EventRepository {
    override fun getAllEvents(): Flow<List<Event>> = dataSource.getAllEvents()
    override suspend fun insertEvent(event: Event): Long = dataSource.insertEvent(event)
    override suspend fun updateEvent(event: Event) = dataSource.updateEvent(event)
    override suspend fun deleteEvent(eventId: Int) = dataSource.deleteEvent(eventId)
    override suspend fun getEventById(eventId: Int): Event? = dataSource.getEventById(eventId)
    override fun searchEvents(query: String): Flow<List<Event>> = dataSource.searchEvents(query)
}