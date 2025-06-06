package com.example.trackernew.data.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val ALARM_CHANNEL = "alarm_channel"
private const val ALARM_NOTIFICATIONS = "Alarm Notifications"
private const val TASK_NAME = "TASK_NAME"
private const val TASK_TIME_UNIT_COUNT = "TASK_TIME_UNIT_COUNT"
private const val TASK_TIME_UNIT = "TASK_TIME_UNIT"
private const val ID = "ID"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            ALARM_CHANNEL,
            ALARM_NOTIFICATIONS,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val taskName = intent.getStringExtra(TASK_NAME)
        val taskTimeUnitCount = intent.getIntExtra(TASK_TIME_UNIT_COUNT, -1)
        val taskTimeUnit = intent.getStringExtra(TASK_TIME_UNIT)

        val notification = NotificationCompat.Builder(context, ALARM_CHANNEL)
            .setContentTitle(taskName)
            .setContentText("До конца дедлайна задачи $taskName осталось $taskTimeUnitCount $taskTimeUnit")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(intent.getIntExtra(ID, 0), notification)
    }
}