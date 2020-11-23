package com.thefuh.markinbook.routes.schools.students.homeworks.tasks

import com.thefuh.markinbook.data.Lesson.Homework.Task
import com.thefuh.markinbook.database.tables.students.homeworks.tasks.TaskEntity
import org.jetbrains.exposed.sql.SizedIterable

fun TaskEntity.toTask(): Task {
    return Task(
        done,
        description,
    )
}

fun SizedIterable<TaskEntity>.toTasks(): List<Task> {
    return map { it.toTask() }
}