package com.thefuh.markinbook.database.tables.schools.students.lessons

import org.jetbrains.exposed.sql.SizedIterable

class LessonsRepository {

    fun getAllByStudentId(studentId: Int): SizedIterable<LessonEntity> {
        return LessonEntity.find { LessonsTable.studentId eq studentId }
    }
}