package io.lb.schedule.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.lb.schedule.domain.repository.EventRepository
import io.lb.schedule.domain.usecase.DeleteEventUseCase
import io.lb.schedule.domain.usecase.EventUseCases
import io.lb.schedule.domain.usecase.GetAllEventsUseCase
import io.lb.schedule.domain.usecase.InsertEventUseCase
import io.lb.schedule.domain.usecase.SearchEventsUseCase
import io.lb.schedule.domain.usecase.UpdateEventUseCase

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun providesEventUseCases(repository: EventRepository): EventUseCases {
        return EventUseCases(
            getAllEventsUseCase = GetAllEventsUseCase(repository),
            insertEventUseCase = InsertEventUseCase(repository),
            updateEventUseCase = UpdateEventUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository),
            searchEventsUseCase = SearchEventsUseCase(repository)
        )
    }
}