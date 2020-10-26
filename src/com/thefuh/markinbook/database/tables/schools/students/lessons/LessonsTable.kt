package com.thefuh.markinbook.database.tables.schools.students.lessons

import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsTable
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesTable
import com.thefuh.markinbook.database.tables.schools.students.StudentsTable
import com.thefuh.markinbook.database.tables.schools.students.homeworks.HomeworksTable
import org.jetbrains.exposed.dao.id.IntIdTable

object LessonsTable : IntIdTable() {
    val studentId = reference("studentId", StudentsTable)
    val groupId = reference("groupId", GroupsTable)
    val disciplineId = reference("disciplineId", DisciplinesTable)
    val start = long("start")
    val durationInMinutes = integer("durationInMinutes")
    val homeworkId = reference("homeworkId", HomeworksTable).nullable()
    val mark = integer("mark").nullable()
}