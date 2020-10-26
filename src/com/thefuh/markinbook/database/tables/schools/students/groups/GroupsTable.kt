package com.thefuh.markinbook.database.tables.schools.students.groups

import org.jetbrains.exposed.dao.id.IntIdTable

object GroupsTable : IntIdTable() {
    val title = text("title")
}
