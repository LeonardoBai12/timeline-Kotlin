package io.lb.schedule.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lb.schedule.data.database.EventDatabase
import io.lb.schedule.data.datasource.EventDataSource
import io.lb.schedule.data.repository.EventRepositoryImpl
import io.lb.schedule.data.service.DatabaseService
import io.lb.schedule.data.service.DatabaseServiceImpl
import io.lb.schedule.domain.repository.EventRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesEventDatabase(app: Application): EventDatabase {
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            EventDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesDatabaseService(database: EventDatabase): DatabaseService {
        return DatabaseServiceImpl(database.eventDao)
    }

    @Provides
    @Singleton
    fun providesEventDataSource(
        databaseService: DatabaseService
    ): EventDataSource {
        return EventDataSource(databaseService)
    }

    @Provides
    @Singleton
    fun providesEventRepository(
        dataSource: EventDataSource
    ): EventRepository {
        return EventRepositoryImpl(dataSource)
    }
}
