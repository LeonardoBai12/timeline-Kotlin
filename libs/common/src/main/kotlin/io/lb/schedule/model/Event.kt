package io.lb.schedule.model

import java.util.Date

data class Event(
    val id: Int,
    val startDate: Date,
    val endDate: Date,
    val name: String
)
