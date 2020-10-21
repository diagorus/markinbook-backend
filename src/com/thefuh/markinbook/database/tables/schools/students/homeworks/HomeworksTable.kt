package com.thefuh.markinbook.database.tables.schools.students.homeworks

import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object HomeworksTable : IntIdTable() {
    val mark = integer("mark").nullable()
    val lessonId = reference("lessonId", LessonsTable)
}