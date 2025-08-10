package io.lb.schedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.lb.schedule.data.dao.EventDao
import io.lb.schedule.data.model.EventEntity

@Database(
    version = 1,
    entities = [EventEntity::class],
    exportSchema = false
)
abstract class EventDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
    
    companion object {
        const val DATABASE_NAME = "event_timeline.db"
    }
}
