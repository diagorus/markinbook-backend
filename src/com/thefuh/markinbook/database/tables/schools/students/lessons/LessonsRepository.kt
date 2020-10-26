package com.thefuh.markinbook.database.tables.schools.students.lessons

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.schools.students.StudentEntity
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupEntity
import org.jetbrains.exposed.sql.SizedIterable

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

    fun getAllByStudentId(studentId: Int): SizedIterable<LessonEntity> {
        return LessonEntity.find { LessonsTable.studentId eq studentId }
    }

    fun getById(id: Int): LessonEntity? {
        return LessonEntity.findById(id)
    }
}