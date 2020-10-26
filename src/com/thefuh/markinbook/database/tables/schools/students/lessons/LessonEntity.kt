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

    var student by StudentEntity referencedOn LessonsTable.studentId
    var group by GroupEntity referencedOn LessonsTable.groupId
    var discipline by DisciplineEntity referencedOn LessonsTable.disciplineId
    var start by LessonsTable.start
    var durationInMinutes by LessonsTable.durationInMinutes
    var homework by HomeworkEntity optionalReferencedOn LessonsTable.homeworkId
    var mark by LessonsTable.mark
}
