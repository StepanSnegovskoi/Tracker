package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.CategoryDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.LessonDbModel
import com.example.trackernew.data.entity.WeekDbModel
import com.example.trackernew.data.mapper.toEntity
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekDao {

    @Insert
    suspend fun addWeek(weekDbModel: WeekDbModel)

    @Query("SELECT * FROM weeks")
    fun getWeeks(): Flow<List<WeekDbModel>>

    @Update
    suspend fun updateWeek(week: WeekDbModel)

    @Query("SELECT * FROM weeks WHERE id = :weekId")
    suspend fun getWeekById(weekId: String): WeekDbModel?

    suspend fun addLessonByWeekIdAndDayName(weekId: String, dayName: String, lessonDbModel: LessonDbModel) {
        val week = getWeekById(weekId) ?: throw Exception("Week not found")
        val updatedDays = week.days.map { day ->
            if (day.name.equals(dayName, ignoreCase = true)) {
                day.copy(lessons = day.lessons + lessonDbModel.toEntity())
            } else {
                day
            }
        }
        val updatedWeek = week.copy(days = updatedDays)
        updateWeek(updatedWeek)
    }
}