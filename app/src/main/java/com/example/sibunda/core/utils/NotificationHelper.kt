package com.example.sibunda.core.utils

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sibunda.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificationHelper {

    private const val CHANNEL_ID = "agenda_posyandu_channel"
    private const val CHANNEL_NAME = "Agenda Posyandu"
    private const val CHANNEL_DESC = "Notifikasi pengingat agenda posyandu SiBunda"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showAgendaNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        createNotificationChannel(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_profile)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    fun scheduleAgendaNotifications(context: Context) {
        createNotificationChannel(context)

        val pref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

        val aktif = pref.getBoolean(Constants.KEY_NOTIF_ENABLED, false)
        val jam = pref.getInt(Constants.KEY_NOTIF_HOUR, 7)
        val menit = pref.getInt(Constants.KEY_NOTIF_MINUTE, 0)
        val sehariSebelum = pref.getBoolean(Constants.KEY_NOTIF_DAY_BEFORE, false)

        cancelAgendaNotifications(context)

        if (!aktif) return

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val now = Calendar.getInstance()

        AgendaDataDummy.listAgenda.forEachIndexed { index, agenda ->
            try {
                val tanggalAgenda = format.parse(agenda.tanggal) ?: return@forEachIndexed

                val calendar = Calendar.getInstance().apply {
                    time = tanggalAgenda
                    set(Calendar.HOUR_OF_DAY, jam)
                    set(Calendar.MINUTE, menit)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    if (sehariSebelum) {
                        add(Calendar.DAY_OF_MONTH, -1)
                    }
                }

                if (calendar.before(now)) {
                    return@forEachIndexed
                }

                val title = if (sehariSebelum) {
                    "Pengingat Agenda Besok"
                } else {
                    "Agenda Posyandu Hari Ini"
                }

                val message = "${agenda.judul}\nLokasi: ${agenda.lokasi}\n${agenda.keterangan}"

                val intent = Intent(context, AgendaNotificationReceiver::class.java).apply {
                    putExtra("title", title)
                    putExtra("message", message)
                    putExtra("notification_id", 1000 + index)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    1000 + index,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

            } catch (_: Exception) {
            }
        }
    }

    fun cancelAgendaNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        AgendaDataDummy.listAgenda.forEachIndexed { index, _ ->
            val intent = Intent(context, AgendaNotificationReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1000 + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
        }
    }
}