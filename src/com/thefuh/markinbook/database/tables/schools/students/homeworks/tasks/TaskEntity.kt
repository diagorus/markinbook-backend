package com.thefuh.markinbook.database.tables.schools.students.homeworks.tasks

import com.thefuh.markinbook.database.tables.schools.students.homeworks.HomeworkEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskEntity>(TasksTable)

    var done by TasksTable.done
    var description by TasksTable.description
    var homework by HomeworkEntity referencedOn TasksTable.homeworkId
}