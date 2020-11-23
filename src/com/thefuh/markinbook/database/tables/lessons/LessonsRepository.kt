package com.thefuh.markinbook.database.tables.lessons

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.students.StudentEntity
import com.thefuh.markinbook.database.tables.students.groups.GroupEntity
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and

class LessonsRepository {

    fun add(
        student: StudentEntity,
        group: GroupEntity,
        discipline: DisciplineEntity,
        start: Long,
        durationInMinutes: Int,
    ): LessonEntity {
        return LessonEntity.new {
            this.student = student
            this.group = group
            this.discipline = discipline
            this.start = start
            this.durationInMinutes = durationInMinutes
        }
    }

    fun getAllForWeek(
        studentId: Int,
        weekStartMillis: Long,
        weekEndMillis: Long
    ): SizedIterable<LessonEntity> {
        return LessonEntity.find {
            (LessonsTable.studentId eq studentId) and
                    (LessonsTable.start greaterEq weekStartMillis) and
                    (LessonsTable.start lessEq weekEndMillis)
        }
    }


    fun getAllByStudentId(studentId: Int): SizedIterable<LessonEntity> {
        return LessonEntity.find { LessonsTable.studentId eq studentId }
    }

    fun getById(id: Int): LessonEntity? {
        return LessonEntity.findById(id)
    }
}