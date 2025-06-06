package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.repository.AddLecturerRepository
import javax.inject.Inject

class AddLecturerUseCase @Inject constructor(
    private val addLecturerRepository: AddLecturerRepository
) {

    suspend operator fun invoke(lecturer: Lecturer) =
        addLecturerRepository.addLecturer(lecturer)
}
