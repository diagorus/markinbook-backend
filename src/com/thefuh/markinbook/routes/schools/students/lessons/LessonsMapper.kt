package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.data.Lesson
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonEntity
import com.thefuh.markinbook.routes.schools.disciplines.toDiscipline
import com.thefuh.markinbook.routes.schools.students.groups.toGroup
import com.thefuh.markinbook.routes.schools.students.homeworks.toHomework
import org.jetbrains.exposed.sql.SizedIterable

fun LessonEntity.toLesson(): Lesson {
    return Lesson(
        group.toGroup(),
        discipline.toDiscipline(),
        start,
        durationInMinutes,
        homework?.toHomework(),
        mark
    )
}

fun SizedIterable<LessonEntity>.toLessons(): List<Lesson> {
    return map { it.toLesson() }
}