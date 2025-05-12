package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackernew.data.entity.LessonDbModel
import com.example.trackernew.data.entity.WeekDbModel
import com.example.trackernew.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekDao {

    @Insert
    suspend fun addWeek(weekDbModel: WeekDbModel)

    @Query("SELECT * FROM weeks")
    fun getWeeks(): Flow<List<WeekDbModel>>

    @Update
    suspend fun updateWeek(week: WeekDbModel)

    @Query("DELETE FROM weeks WHERE id =:id")
    suspend fun deleteWeek(id: Int)

    @Query("SELECT * FROM weeks WHERE id = :weekId")
    suspend fun getWeekById(weekId: Int): WeekDbModel?

    suspend fun addLessonByWeekIdAndDayName(weekId: Int, dayName: String, lessonDbModel: LessonDbModel) {
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

    suspend fun deleteLessonByLessonId(weekId: Int, lessonId: Int) {
        val week = getWeekById(weekId) ?: throw Exception("Week not found")

        val updatedDays = week.days.map { day ->

            val updatedLessons = day.lessons.filter { it.id != lessonId }
            day.copy(lessons = updatedLessons)
        }

        val updatedWeek = week.copy(days = updatedDays)
        updateWeek(updatedWeek)
    }
}