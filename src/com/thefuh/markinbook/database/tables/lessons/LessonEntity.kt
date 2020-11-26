package com.thefuh.markinbook.database.tables.lessons

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.students.StudentEntity
import com.thefuh.markinbook.database.tables.students.groups.GroupEntity
import com.thefuh.markinbook.database.tables.students.homeworks.HomeworkEntity
import com.thefuh.markinbook.database.tables.teachers.TeacherEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LessonEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LessonEntity>(LessonsTable)

    var teacher by TeacherEntity referencedOn LessonsTable.teacherId
    var group by GroupEntity referencedOn LessonsTable.groupId
    var discipline by DisciplineEntity referencedOn LessonsTable.disciplineId
    var start by LessonsTable.start
    var durationInMinutes by LessonsTable.durationInMinutes
    var homework by HomeworkEntity optionalReferencedOn LessonsTable.homeworkId
    var mark by LessonsTable.mark
}
