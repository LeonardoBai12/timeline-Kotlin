package io.lb.schedule.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.lb.schedule.model.Event
import java.util.Date

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val startDate: Long,
    val endDate: Long
) {
    fun toEvent() = Event(
        id = id,
        name = name,
        startDate = Date(startDate),
        endDate = Date(endDate)
    )

    companion object {
        fun fromEvent(event: Event) = EventEntity(
            id = if (event.id == 0) 0 else event.id,
            name = event.name,
            startDate = event.startDate.time,
            endDate = event.endDate.time
        )
    }
}