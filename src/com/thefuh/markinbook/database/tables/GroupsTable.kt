package com.thefuh.markinbook.database.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object GroupsTable : IntIdTable() {
    val title = varchar("title", 128)
}

