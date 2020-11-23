package com.thefuh.markinbook.database.tables.students

import com.thefuh.markinbook.database.tables.schools.SchoolsTable
import com.thefuh.markinbook.database.tables.students.groups.GroupsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object StudentsTable : IntIdTable() {
    val firstName = text("firstName")
    val lastName = text("lastName")

    val schoolId = reference("schoolId", SchoolsTable)
    val groupId = reference("groupId", GroupsTable)
}