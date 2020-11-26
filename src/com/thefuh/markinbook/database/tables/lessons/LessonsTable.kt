package com.thefuh.markinbook.database.tables.lessons

import com.thefuh.markinbook.database.tables.students.groups.GroupsTable
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesTable
import com.thefuh.markinbook.database.tables.students.StudentsTable
import com.thefuh.markinbook.database.tables.students.homeworks.HomeworksTable
import com.thefuh.markinbook.database.tables.teachers.TeachersTable
import org.jetbrains.exposed.dao.id.IntIdTable

object LessonsTable : IntIdTable() {
    val teacherId = reference("teacherId", TeachersTable)
    val groupId = reference("groupId", GroupsTable)
    val disciplineId = reference("disciplineId", DisciplinesTable)
    val start = long("start")
    val durationInMinutes = integer("durationInMinutes")
    val homeworkId = reference("homeworkId", HomeworksTable).nullable()
    val mark = integer("mark").nullable()
}