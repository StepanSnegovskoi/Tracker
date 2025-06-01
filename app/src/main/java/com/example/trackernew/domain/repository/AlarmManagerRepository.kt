package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Task

interface AlarmManagerRepository {

    fun setAlarm(task: Task, time: Long, code: Int)

    fun cancelAlarm(code: Int)
}