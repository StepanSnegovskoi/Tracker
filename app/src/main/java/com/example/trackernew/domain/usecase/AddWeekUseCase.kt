package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.AddAudienceRepository
import com.example.trackernew.domain.repository.AddLecturerRepository
import com.example.trackernew.domain.repository.AddLessonNameRepository
import com.example.trackernew.domain.repository.WeekRepository
import javax.inject.Inject

class AddWeekUseCase @Inject constructor(
    private val weekRepository: WeekRepository
) {

    suspend operator fun invoke(week: Week) = weekRepository.addWeek(week)
}
