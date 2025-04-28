package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.data.entity.CategoryDbModel
import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.data.entity.WeekDbModel
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekDao {

    @Insert
    suspend fun addWeek(weekDbModel: WeekDbModel)

    @Query("SELECT * FROM weeks")
    fun getWeeks(): Flow<List<WeekDbModel>>
}