package io.lb.schedule.domain.repository

import io.lb.schedule.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getAllEvents(): Flow<List<Event>>
    suspend fun insertEvent(event: Event): Long
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(eventId: Int)
    suspend fun getEventById(eventId: Int): Event?
    fun searchEvents(query: String): Flow<List<Event>>
}