package com.example.trackernew.data.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "alarm_channel",
            "Alarm Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val taskName = intent.getStringExtra("TASK_NAME")
        val taskTimeUnitCount = intent.getIntExtra("TASK_TIME_UNIT_COUNT", -1)
        val taskTimeUnit = intent.getStringExtra("TASK_TIME_UNIT")

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle(taskName)
            .setContentText("До конца дедлайна задачи $taskName осталось $taskTimeUnitCount $taskTimeUnit")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(intent.getIntExtra("ID", 0), notification)
    }
}