package com.thefuh.markinbook.database.tables.schools.students.lessons

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.schools.students.StudentEntity
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupEntity
import com.thefuh.markinbook.database.tables.schools.students.homeworks.HomeworkEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LessonEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LessonEntity>(LessonsTable)

    val student by StudentEntity referencedOn LessonsTable.studentId
    val group by GroupEntity referencedOn LessonsTable.groupId
    val discipline by DisciplineEntity referencedOn LessonsTable.disciplineId
    val start by LessonsTable.start
    val durationInMinutes by LessonsTable.durationInMinutes
    val homework by HomeworkEntity optionalReferencedOn LessonsTable.homeworkId
    val mark by LessonsTable.mark
}
