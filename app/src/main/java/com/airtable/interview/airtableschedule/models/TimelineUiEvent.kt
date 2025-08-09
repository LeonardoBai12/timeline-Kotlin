package com.airtable.interview.airtableschedule.models

sealed class TimelineUiEvent {
    data class ShowError(val message: String) : TimelineUiEvent()
    data class ShowSuccess(val message: String) : TimelineUiEvent()
    data object EventSaved : TimelineUiEvent()
    data object EventDeleted : TimelineUiEvent()
}