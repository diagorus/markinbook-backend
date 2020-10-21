package com.thefuh.markinbook.database.tables.schools.disciplines

import com.thefuh.markinbook.database.tables.schools.SchoolsTable
import org.jetbrains.exposed.dao.id.IntIdTable

object DisciplinesTable : IntIdTable() {
    val title = text("title")
    val schoolId = reference("schoolId", SchoolsTable)
}