package com.thefuh.markinbook.database.tables.students.homeworks.tasks

import com.thefuh.markinbook.database.tables.students.homeworks.HomeworksTable
import org.jetbrains.exposed.dao.id.IntIdTable

object TasksTable : IntIdTable() {
    val done = bool("done")
    val description = varchar("description", 512)
    val homeworkId = reference("homeworkId", HomeworksTable)
}