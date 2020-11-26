package com.thefuh.markinbook.database.tables.lessons

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.students.groups.GroupEntity
import com.thefuh.markinbook.database.tables.teachers.TeacherEntity
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and

class LessonsRepository {

    fun add(
        teacher: TeacherEntity,
        group: GroupEntity,
        discipline: DisciplineEntity,
        start: Long,
        durationInMinutes: Int,
    ): LessonEntity {
        return LessonEntity.new {
            this.teacher = teacher
            this.group = group
            this.discipline = discipline
            this.start = start
            this.durationInMinutes = durationInMinutes
        }
    }

    fun getAllForWeekForTeacher(
        teacherId: Int,
        weekStartMillis: Long,
        weekEndMillis: Long
    ): SizedIterable<LessonEntity> {
        return LessonEntity.find {
            (LessonsTable.teacherId eq teacherId) and
                    (LessonsTable.start greaterEq weekStartMillis) and
                    (LessonsTable.start lessEq weekEndMillis)
        }
    }

    fun getAllForWeekForGroup(
        groupId: Int,
        weekStartMillis: Long,
        weekEndMillis: Long
    ): SizedIterable<LessonEntity> {
        return LessonEntity.find {
            (LessonsTable.groupId eq groupId) and
                    (LessonsTable.start greaterEq weekStartMillis) and
                    (LessonsTable.start lessEq weekEndMillis)
        }
    }


    fun getAllByTeacherId(teacherId: Int): SizedIterable<LessonEntity> {
        return LessonEntity.find { LessonsTable.teacherId eq teacherId }
    }

    fun getById(id: Int): LessonEntity? {
        return LessonEntity.findById(id)
    }
}