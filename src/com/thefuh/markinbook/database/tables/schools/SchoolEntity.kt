package com.thefuh.markinbook.database.tables.schools

import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SchoolEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SchoolEntity>(SchoolsTable)

    var title by SchoolsTable.title

    val disciplines by DisciplineEntity referrersOn DisciplinesTable.schoolId
}