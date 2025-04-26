package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.domain.entity.TypeOfLesson

class TypeOfLessonConverter {

    @TypeConverter
    fun fromTypeOfLesson(typeOfLesson: TypeOfLesson): String {
        return when (typeOfLesson) {
            TypeOfLesson.Another -> "Другое"
            TypeOfLesson.Lesson -> "Лекция"
            TypeOfLesson.Practise -> "Практика"
        }
    }

    @TypeConverter
    fun fromJson(typeOfLessonJson: String): TypeOfLesson {
        return when (typeOfLessonJson) {
            "Другое" -> TypeOfLesson.Another
            "Лекция" -> TypeOfLesson.Lesson
            "Практика" -> TypeOfLesson.Practise
            else -> throw IllegalArgumentException("Unknown TypeOfLesson: $typeOfLessonJson")
        }
    }
}