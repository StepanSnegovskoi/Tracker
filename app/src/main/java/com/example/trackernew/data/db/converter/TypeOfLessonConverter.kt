package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.domain.entity.TypeOfLesson

class TypeOfLessonConverter {

    @TypeConverter
    fun fromStatus(typeOfLesson: TypeOfLesson): String {
        return when (typeOfLesson) {
            TypeOfLesson.Lesson -> "Lesson"
            TypeOfLesson.Practise -> "Practise"
            TypeOfLesson.Another -> "Another"
        }
    }

    @TypeConverter
    fun fromJson(typeOfLessonJson: String): TypeOfLesson {
        return when (typeOfLessonJson) {
            "Lesson" -> TypeOfLesson.Lesson
            "Practise" -> TypeOfLesson.Practise
            "Another" -> TypeOfLesson.Another
            else -> throw IllegalArgumentException("Unknown TypeOfLesson: $typeOfLessonJson")
        }
    }
}