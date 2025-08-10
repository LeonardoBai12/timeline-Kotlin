package io.lb.schedule.data.service

import io.lb.schedule.model.Event
import kotlinx.coroutines.flow.Flow

interface DatabaseService {
    fun getAllEvents(): Flow<List<Event>>
    suspend fun insertEvent(event: Event): Long
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(eventId: Int)
    suspend fun getEventById(eventId: Int): Event?
    fun searchEvents(query: String): Flow<List<Event>>
}
