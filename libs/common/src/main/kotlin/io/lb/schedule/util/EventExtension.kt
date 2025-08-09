package io.lb.schedule.util

import io.lb.schedule.model.Event

fun List<Event>.filterBy(filter: String) =
    filter {
        filter.lowercase() in it.name.lowercase()
    }
