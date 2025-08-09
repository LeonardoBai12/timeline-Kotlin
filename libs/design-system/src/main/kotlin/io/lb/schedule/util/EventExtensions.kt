package io.lb.schedule.util

import io.lb.schedule.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.daysBetween(endDate: Date): Int {
    val diffInMillis = endDate.time - this.time
    return (diffInMillis / (1000 * 60 * 60 * 24)).toInt() + 1
}

fun Event.getDurationInDays(): Int {
    return startDate.daysBetween(endDate)
}

fun Date.toTimelineFormat(): String {
    return SimpleDateFormat("MMM dd", Locale.getDefault()).format(this)
}

fun Event.getDateRangeText(): String {
    val start = startDate.toTimelineFormat()
    val end = endDate.toTimelineFormat()
    return if (start == end) start else "$start - $end"
}

fun Event.isSingleDay(): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = startDate }
    val calendar2 = Calendar.getInstance().apply { time = endDate }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun Event.getTimelineOffset(minDate: Date, pixelsPerDay: Int = 40): Int {
    val daysDiff = ((startDate.time - minDate.time) / (1000 * 60 * 60 * 24)).toInt()
    return daysDiff * pixelsPerDay
}

fun Event.getTimelineWidth(pixelsPerDay: Int = 40): Int {
    return getDurationInDays() * pixelsPerDay
}