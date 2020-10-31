package com.thefuh.markinbook.database.tables.schools.students.groups

import com.thefuh.markinbook.database.tables.schools.SchoolsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object GroupsTable : IntIdTable() {
    val title = text("title")
    val schoolId = reference("schoolId", SchoolsTable)
}
