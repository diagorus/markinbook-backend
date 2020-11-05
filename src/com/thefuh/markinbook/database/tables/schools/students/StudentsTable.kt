package com.thefuh.markinbook.database.tables.schools.students

import com.thefuh.markinbook.database.tables.schools.SchoolsTable
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object StudentsTable : IntIdTable() {
    val firstName = text("firstName")
    val lastName = text("lastName")

    val schoolId = reference("schoolId", SchoolsTable)
    val groupId = reference("groupId", GroupsTable)
}