package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.LessonDbModel
import com.example.trackernew.data.entity.LessonNameDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Insert
    suspend fun addLesson(lessonDbModel: LessonDbModel)

    @Insert
    suspend fun addLessonName(lessonNameDbModel: LessonNameDbModel)

    @Query("SELECT * FROM lessonNames")
    fun getLessonNames(): Flow<List<LessonNameDbModel>>

    @Insert
    suspend fun addAudience(audienceDbModel: AudienceDbModel)

    @Query("SELECT * FROM audiences")
    fun getAudiences(): Flow<List<AudienceDbModel>>

    @Insert
    suspend fun addLecturer(lecturerDbModel: LecturerDbModel)

    @Query("SELECT * FROM lecturers")
    fun getLecturers(): Flow<List<LecturerDbModel>>
}