package com.thefuh.markinbook.database.tables.schools.students

import com.thefuh.markinbook.database.tables.GroupsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object StudentsTable : IntIdTable() {
    val firstName = text("firstName")
    val lastName = text("lastName")
    val middleName = text("middleName")

    val groupId = reference("groupId", GroupsTable)
}