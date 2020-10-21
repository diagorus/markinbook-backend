package com.thefuh.markinbook.database.tables.schools.students.homeworks

import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonEntity
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsTable
import com.thefuh.markinbook.database.tables.schools.students.homeworks.tasks.TaskEntity
import com.thefuh.markinbook.database.tables.schools.students.homeworks.tasks.TasksTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class HomeworkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HomeworkEntity>(HomeworksTable)

    var mark by HomeworksTable.mark
    var lesson by LessonEntity optionalReferencedOn LessonsTable.homeworkId
    val tasks by TaskEntity referrersOn TasksTable.homeworkId
}