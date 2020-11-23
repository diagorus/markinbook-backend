package com.thefuh.markinbook.database.tables.teachers

import com.thefuh.markinbook.database.tables.schools.SchoolsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object TeachersTable : IntIdTable() {

    val firstName = text("firstName")
    val lastName = text("lastName")

    val schoolId = reference("schoolId", SchoolsTable)
}