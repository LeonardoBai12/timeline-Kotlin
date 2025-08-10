package io.lb.schedule.data.datasource

import io.lb.schedule.data.service.DatabaseService
import io.lb.schedule.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventDataSource @Inject constructor(
    private val databaseService: DatabaseService,
) {
    fun getAllEvents(): Flow<List<Event>> = databaseService.getAllEvents()
    suspend fun insertEvent(event: Event): Long = databaseService.insertEvent(event)
    suspend fun updateEvent(event: Event) = databaseService.updateEvent(event)
    suspend fun deleteEvent(eventId: Int) = databaseService.deleteEvent(eventId)
    suspend fun getEventById(eventId: Int): Event? = databaseService.getEventById(eventId)
    fun searchEvents(query: String): Flow<List<Event>> = databaseService.searchEvents(query)
}
