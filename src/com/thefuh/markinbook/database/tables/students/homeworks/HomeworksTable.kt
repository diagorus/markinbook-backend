package com.thefuh.markinbook.database.tables.students.homeworks

import com.thefuh.markinbook.database.tables.lessons.LessonsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object HomeworksTable : IntIdTable() {
    val mark = integer("mark").nullable()
    val lessonId = reference("lessonId", LessonsTable)
}