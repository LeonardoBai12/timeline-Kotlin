package io.lb.schedule.util

import android.content.Context

class EventToaster(private val context: Context) : Toaster {
    override fun showToast(message: String) {
        context.showToast(message)
    }

    override fun showToast(resId: Int) {
        context.showToast(resId)
    }
}
