package com.example.sibunda.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AgendaNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Pengingat Posyandu"
        val message = intent.getStringExtra("message") ?: "Ada agenda posyandu yang perlu diperhatikan."
        val notificationId = intent.getIntExtra("notification_id", System.currentTimeMillis().toInt())

        NotificationHelper.showAgendaNotification(
            context = context,
            title = title,
            message = message,
            notificationId = notificationId
        )
    }
}