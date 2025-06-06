package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.LessonNameDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLessonName(lessonNameDbModel: LessonNameDbModel)

    @Query("SELECT * FROM lessonNames")
    fun getLessonNames(): Flow<List<LessonNameDbModel>>

    @Query("DELETE FROM lessonNames WHERE name =:name")
    suspend fun deleteLessonName(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAudience(audienceDbModel: AudienceDbModel)

    @Query("SELECT * FROM audiences")
    fun getAudiences(): Flow<List<AudienceDbModel>>

    @Query("DELETE FROM audiences WHERE name =:name")
    suspend fun deleteAudience(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLecturer(lecturerDbModel: LecturerDbModel)

    @Query("SELECT * FROM lecturers")
    fun getLecturers(): Flow<List<LecturerDbModel>>

    @Query("DELETE FROM lecturers WHERE name =:name")
    suspend fun deleteLecturer(name: String)
}