package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.LessonDbModel
import com.example.trackernew.data.entity.LessonNameDbModel
import com.example.trackernew.domain.entity.Audience

@Dao
interface LessonDao {

    @Insert
    suspend fun addLesson(lessonDbModel: LessonDbModel)

    @Insert
    suspend fun addLessonName(lessonNameDbModel: LessonNameDbModel)

    @Insert
    suspend fun addAudience(audienceDbModel: AudienceDbModel)

    @Insert
    suspend fun addLecturer(lecturerDbModel: LecturerDbModel)
}