package com.thefuh.markinbook.database.tables.schools

import org.jetbrains.exposed.dao.id.IntIdTable

object SchoolsTable : IntIdTable() {
    val title = text("title")
}