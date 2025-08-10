package io.lb.schedule.util

import io.lb.schedule.model.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toTimelineFormat(): String {
    return SimpleDateFormat("MMM dd", Locale.getDefault()).format(this)
}

fun Event.getDateRangeText(): String {
    val start = startDate.toTimelineFormat()
    val end = endDate.toTimelineFormat()
    return if (start == end) start else "$start - $end"
}
