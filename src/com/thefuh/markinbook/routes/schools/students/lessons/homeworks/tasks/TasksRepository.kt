package com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks

import com.thefuh.markinbook.data.lesson.homework.Task
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworkEntity
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable

class TasksRepository {

    fun add(homework: HomeworkEntity, description: String) : TaskEntity {
        return TaskEntity.new {
            this.homework = homework
            this.description = description
        }
    }
}

object TasksTable : IntIdTable() {
    val description = varchar("description", 512)
    val homeworkId = reference("homeworkId", HomeworksTable)
}

class TaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskEntity>(TasksTable)

    var description by TasksTable.description
    var homework by HomeworkEntity referencedOn TasksTable.homeworkId
}

fun TaskEntity.toTask(): Task {
    return Task(
        description,
    )
}

fun SizedIterable<TaskEntity>.toTasks(): List<Task> {
    return map { it.toTask() }
}