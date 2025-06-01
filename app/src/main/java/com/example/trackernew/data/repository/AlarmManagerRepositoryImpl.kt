package com.example.trackernew.data.repository

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trackernew.data.receiver.AlarmReceiver
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.AlarmManagerRepository
import javax.inject.Inject


class AlarmManagerRepositoryImpl @Inject constructor(private val context: Context) :
    AlarmManagerRepository {

    override fun setAlarm(
        task: Task,
        time: Long,
        code: Int
    ) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ID", code)
            putExtra("TASK_NAME", task.name)
            putExtra("TASK_TIME_UNIT_COUNT", task.timeUnitCount)
            putExtra("TASK_TIME_UNIT", task.timeUnit)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    override fun cancelAlarm(code: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}